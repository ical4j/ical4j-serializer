package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VEvent
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaEventSerializerTest extends AbstractSerializerTest {

    def 'test event serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VEvent, new SchemaEventSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the event is serialized'
        String serialized = mapper.writeValueAsString(event)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        event   | expectedSerialized
        event1  | '{"@context":"https://schema.org","@type":"Event","@id":"1","name":"Test Event 1","startDate":"20090810"}'
        event2  | '{"@context":"https://schema.org","@type":"Event","@id":"2","name":"Test Event 2","startDate":"20100810"}'
    }
}
