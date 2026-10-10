package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification
import spock.lang.Unroll

class ParticipantBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    JsonNode participant(String sendTo) {
        target.get('participants').elements().find {
            (it.get('sendTo').path('imip').textValue() ?: it.get('sendTo').path('other').textValue()) == sendTo
        }
    }

    def 'organizer is owner and attendee is attendee'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            organizer 'mailto:a@example.com'
            attendee 'mailto:b@example.com'
        }

        when:
        ParticipantBuilder.applyTo(event, target)

        then:
        target.get('participants').size() == 2
        def a = participant('mailto:a@example.com')
        a.get('@type').asText() == 'Participant'
        a.get('roles').get('owner').asBoolean()
        !a.get('roles').has('attendee')

        def b = participant('mailto:b@example.com')
        b.get('roles').get('attendee').asBoolean()
        !b.get('roles').has('owner')
    }

    def 'participant ids are valid JSCalendar ids and stable'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            attendee 'mailto:b@example.com'
        }
        ObjectNode other = new ObjectMapper().createObjectNode()

        when:
        ParticipantBuilder.applyTo(event, target)
        ParticipantBuilder.applyTo(event, other)
        def id = target.get('participants').fieldNames().next()

        then:
        id ==~ /[A-Za-z0-9_-]{1,255}/
        other.get('participants').fieldNames().next() == id
    }

    def 'organizer who also attends is merged regardless of address case'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            organizer 'MAILTO:A@Example.com'
            attendee 'mailto:a@example.com'
        }

        when:
        ParticipantBuilder.applyTo(event, target)

        then:
        target.get('participants').size() == 1
        def a = participant('mailto:a@example.com')
        a.get('roles').get('owner').asBoolean()
        a.get('roles').get('attendee').asBoolean()
    }

    def 'non-mailto address is sent via other rather than imip'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            attendee 'urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
        }

        when:
        ParticipantBuilder.applyTo(event, target)
        def sendTo = target.get('participants').elements().next().get('sendTo')

        then:
        sendTo.get('other').asText() == 'urn:uuid:f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
        !sendTo.has('imip')
    }

    @Unroll
    def 'attendee ROLE=#role maps to roles #expected'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            attendee 'mailto:x@example.com', parameters: builder.parameters { delegate.role role }
        }

        when:
        ParticipantBuilder.applyTo(event, target)
        def roles = participant('mailto:x@example.com').get('roles')

        then:
        roles.fieldNames().toList().sort() == expected

        where:
        role              | expected
        'CHAIR'           | ['attendee', 'chair']
        'REQ-PARTICIPANT' | ['attendee']
        'OPT-PARTICIPANT' | ['attendee', 'optional']
        'NON-PARTICIPANT' | ['informational']
    }

    def 'no participants property when none present'() {
        given:
        VEvent event = builder.vevent { uid '1' }

        when:
        ParticipantBuilder.applyTo(event, target)

        then:
        !target.has('participants')
    }
}
