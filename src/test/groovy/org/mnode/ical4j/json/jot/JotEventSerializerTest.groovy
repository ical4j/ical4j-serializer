package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VEvent
import org.mnode.ical4j.json.AbstractSerializerTest

class JotEventSerializerTest extends AbstractSerializerTest {

    def 'test event serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VEvent, new JotEventSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a event is serialized'
        String serialized = mapper.writeValueAsString(event)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        event    | expectedSerialized
        event1   | '{"uid":"1"}'
        event2   | '{"uid":"2"}'
    }
}
