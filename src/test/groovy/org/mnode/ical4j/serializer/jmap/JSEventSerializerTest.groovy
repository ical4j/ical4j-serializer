package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class JSEventSerializerTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JSCalendarModule())

    ContentBuilder builder = new ContentBuilder()

    def 'serializes minimal event with UTC start'() {
        given:
        VEvent event = builder.vevent {
            uid 'abc@example.com'
            summary 'Meeting'
            dtstart '20240516T090000Z'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('@type').asText() == 'jsevent'
        node.get('uid').asText() == 'abc@example.com'
        node.get('title').asText() == 'Meeting'
        node.get('start').asText() == '2024-05-16T09:00:00'
        node.get('timeZone').asText() == 'Etc/UTC'
    }

    def 'serializes event with explicit time zone'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000', parameters: parameters { tzid_ 'Australia/Sydney' }
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('start').asText() == '2024-05-16T09:00:00'
        node.get('timeZone').asText() == 'Australia/Sydney'
    }

    def 'serializes floating-time event without timeZone'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('start').asText() == '2024-05-16T09:00:00'
        !node.has('timeZone')
    }

    def 'serializes all-day event with showWithoutTime'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516', parameters: parameters { value 'DATE' }
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('start').asText() == '2024-05-16T00:00:00'
        node.get('showWithoutTime').asBoolean()
        !node.has('timeZone')
    }

    def 'serializes event with explicit duration'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            duration 'PT1H30M'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('duration').asText() == 'PT1H30M'
    }

    def 'serializes event with DTEND derived duration'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            dtend '20240516T103000Z'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('duration').asText() == 'PT1H30M'
        !node.has('end')
        !node.has('dtend')
    }

    def 'serializes categories as keywords'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            categories 'meeting,work'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('keywords').get('meeting').asBoolean()
        node.get('keywords').get('work').asBoolean()
    }

    def 'serializes created and last-modified timestamps with Z suffix'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            created '20240101T120000Z'
            lastmodified '20240210T080000Z'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('created').asText() == '2024-01-01T12:00:00Z'
        node.get('updated').asText() == '2024-02-10T08:00:00Z'
    }

    def 'remaps STATUS values to JSCalendar'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            status 'CONFIRMED'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        node.get('status').asText() == 'confirmed'
    }

    def 'serializes organizer and attendee as participants'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            organizer 'mailto:a@example.com'
            attendee 'mailto:b@example.com'
        }

        when:
        JsonNode node = mapper.valueToTree(event)
        def participants = node.get('participants')

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

    def 'tolerates unknown X- properties'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            dtstart '20240516T090000Z'
            'X-CUSTOM' 'foo'
        }

        when:
        JsonNode node = mapper.valueToTree(event)

        then:
        noExceptionThrown()
        !node.has('x-custom')
        !node.has('X-CUSTOM')
    }
}
