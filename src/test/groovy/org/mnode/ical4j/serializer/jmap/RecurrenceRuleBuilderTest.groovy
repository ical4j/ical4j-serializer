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
