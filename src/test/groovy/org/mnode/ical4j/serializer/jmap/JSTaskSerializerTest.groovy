package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VToDo
import spock.lang.Specification

class JSTaskSerializerTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JSCalendarModule())

    ContentBuilder builder = new ContentBuilder()

    def 'serializes task with due date'() {
        given:
        VToDo todo = builder.vtodo {
            uid 't1'
            summary 'Pay bills'
            due '20240520T170000Z'
        }

        when:
        JsonNode node = mapper.valueToTree(todo)

        then:
        node.get('@type').asText() == 'jstask'
        node.get('uid').asText() == 't1'
        node.get('title').asText() == 'Pay bills'
        node.get('due').asText() == '2024-05-20T17:00:00'
        node.get('timeZone').asText() == 'Etc/UTC'
    }

    def 'serializes task status as progress'() {
        given:
        VToDo todo = builder.vtodo {
            uid 't1'
            status 'COMPLETED'
        }

        when:
        JsonNode node = mapper.valueToTree(todo)

        then:
        node.get('progress').asText() == 'completed'
        !node.has('status')
    }

    def 'DUE in a different zone is expressed in the start time zone'() {
        given:
        VToDo todo = builder.vtodo {
            uid 't1'
            dtstart '20250101T090000', parameters: parameters { tzid_ 'America/New_York' }
            due '20250101T170000', parameters: parameters { tzid_ 'Europe/London' }
        }

        when:
        JsonNode node = mapper.valueToTree(todo)

        then: '17:00 London is 12:00 New York'
        node.get('timeZone').asText() == 'America/New_York'
        node.get('start').asText() == '2025-01-01T09:00:00'
        node.get('due').asText() == '2025-01-01T12:00:00'
    }

    def 'zoned DUE does not pin a floating start to its zone'() {
        given:
        VToDo todo = builder.vtodo {
            uid 't1'
            dtstart '20250101T090000'
            due '20250101T170000', parameters: parameters { tzid_ 'Europe/London' }
        }

        when:
        JsonNode node = mapper.valueToTree(todo)

        then:
        !node.has('timeZone')
        node.get('start').asText() == '2025-01-01T09:00:00'
    }

    def 'serializes percent-complete'() {
        given:
        VToDo todo = builder.vtodo {
            uid 't1'
            percentcomplete '50'
        }

        when:
        JsonNode node = mapper.valueToTree(todo)

        then:
        node.get('percentComplete').asInt() == 50
    }
}
