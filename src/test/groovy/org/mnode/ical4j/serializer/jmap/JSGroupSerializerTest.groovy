package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import spock.lang.Specification

class JSGroupSerializerTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JSCalendarModule())

    ContentBuilder builder = new ContentBuilder()

    def 'serializes calendar as Group with entries'() {
        given:
        Calendar calendar = builder.calendar {
            prodid '-//Ben Fortuna//iCal4j 3.1//EN'
            version '2.0'
            uid '123'
            vevent {
                uid 'event-1'
                dtstart '20240516T090000Z'
            }
            vtodo {
                uid 'todo-1'
                due '20240520T170000Z'
            }
        }

        when:
        JsonNode node = mapper.valueToTree(calendar)

        then:
        node.get('@type').asText() == 'Group'
        node.get('prodId').asText() == '-//Ben Fortuna//iCal4j 3.1//EN'
        node.get('uid').asText() == '123'

        def entries = node.get('entries')
        entries.isArray()
        entries.size() == 2
        entries.get(0).get('@type').asText() == 'jsevent'
        entries.get(0).get('uid').asText() == 'event-1'
        entries.get(1).get('@type').asText() == 'jstask'
        entries.get(1).get('uid').asText() == 'todo-1'
    }
}
