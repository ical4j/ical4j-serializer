package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAvailability
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaServiceSerializerTest extends AbstractSerializerTest {

    def 'test service serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VAvailability, new SchemaServiceSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the service is serialized'
        String serialized = mapper.writeValueAsString(availability)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        availability   | expectedSerialized
        availability1  | '{"@context":"https://schema.org","@type":"Service","@id":"123"}'
    }
}
