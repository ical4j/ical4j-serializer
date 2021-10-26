package org.mnode.ical4j.json.jscalendar

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VEvent
import org.mnode.ical4j.json.AbstractSerializerTest
import org.mnode.ical4j.json.jscalendar.JSEventSerializer

class JSEventSerializerTest extends AbstractSerializerTest {

    def 'test event serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VEvent, new JSEventSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the event is serialized'
        String serialized = mapper.writeValueAsString(event)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        event   | expectedSerialized
        event1  | '{"@type":"jsevent"}'
        event2  | '{"@type":"jsevent"}'
    }
}
