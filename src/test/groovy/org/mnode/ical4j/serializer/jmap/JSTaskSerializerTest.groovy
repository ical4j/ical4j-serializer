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
