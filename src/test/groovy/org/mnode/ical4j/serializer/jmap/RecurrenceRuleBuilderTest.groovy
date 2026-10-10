package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class RecurrenceRuleBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    def 'weekly recurrence with interval and count'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            rrule 'FREQ=WEEKLY;INTERVAL=2;COUNT=10'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)
        def rule = target.get('recurrenceRules').get(0)

        then:
        rule.get('@type').asText() == 'RecurrenceRule'
        rule.get('frequency').asText() == 'weekly'
        rule.get('interval').asInt() == 2
        rule.get('count').asInt() == 10
    }

    def 'daily recurrence with EXDATE produces excluded override'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            rrule 'FREQ=DAILY'
            exdate '20240520T090000Z'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)

        then:
        target.get('recurrenceRules').get(0).get('frequency').asText() == 'daily'
        target.get('recurrenceOverrides').get('2024-05-20T09:00:00').get('excluded').asBoolean()
    }

    def 'monthly by-day rule emits structured NDay'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            rrule 'FREQ=MONTHLY;BYDAY=1MO'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)
        def rule = target.get('recurrenceRules').get(0)

        then:
        rule.get('frequency').asText() == 'monthly'
        def nday = rule.get('byDay').get(0)
        nday.get('@type').asText() == 'NDay'
        nday.get('day').asText() == 'mo'
        nday.get('nthOfPeriod').asInt() == 1
    }

    def 'RDATE produces empty-object override'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            rdate '20240601T090000Z'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)

        then:
        def override = target.get('recurrenceOverrides').get('2024-06-01T09:00:00')
        override != null
        override.size() == 0
    }

    def 'UTC EXDATE and UNTIL are converted into the start time zone'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20250101T090000', parameters: parameters { tzid_ 'America/New_York' }
            rrule 'FREQ=DAILY;UNTIL=20250110T140000Z'
            exdate '20250108T140000Z'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)

        then: 'values are local times in New York (UTC-5), matching the recurrence instances'
        target.get('recurrenceRules').get(0).get('until').asText() == '2025-01-10T09:00:00'
        target.get('recurrenceOverrides').get('2025-01-08T09:00:00').get('excluded').asBoolean()
        !target.get('recurrenceOverrides').has('2025-01-08T14:00:00')
    }

    def 'time, week number and week start rule parts are preserved'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            rrule 'FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO;BYHOUR=9,17;BYMINUTE=0,30;BYSECOND=15;WKST=SU'
        }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)
        def rule = target.get('recurrenceRules').get(0)

        then:
        rule.get('byWeekNo')*.asInt() == [20]
        rule.get('byHour')*.asInt() == [9, 17]
        rule.get('byMinute')*.asInt() == [0, 30]
        rule.get('bySecond')*.asInt() == [15]
        rule.get('firstDayOfWeek').asText() == 'su'
    }

    def 'no recurrence fields when source is empty'() {
        given:
        VEvent event = builder.vevent { uid '1' }

        when:
        RecurrenceRuleBuilder.applyTo(event, target)

        then:
        !target.has('recurrenceRules')
        !target.has('recurrenceOverrides')
    }
}
