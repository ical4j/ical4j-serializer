package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class ParticipantBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    def 'organizer and attendee become participants keyed by mailto-stripped address'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            organizer 'mailto:a@example.com'
            attendee 'mailto:b@example.com'
        }

        when:
        ParticipantBuilder.applyTo(event, target)
        def participants = target.get('participants')

        then:
        participants.size() == 2
        def a = participants.get('a@example.com')
        a.get('@type').asText() == 'Participant'
        a.get('sendTo').get('imip').asText() == 'mailto:a@example.com'
        a.get('roles').get('owner').asBoolean()
        a.get('roles').get('attendee').asBoolean()

        def b = participants.get('b@example.com')
        b.get('roles').get('attendee').asBoolean()
        !b.get('roles').has('owner')
    }

    def 'attendee with ROLE=CHAIR adds chair role'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            attendee 'mailto:chair@example.com', parameters: builder.parameters { role 'CHAIR' }
        }

        when:
        ParticipantBuilder.applyTo(event, target)

        then:
        target.get('participants').get('chair@example.com').get('roles').get('chair').asBoolean()
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
