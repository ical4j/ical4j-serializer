package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAvailability
import org.mnode.ical4j.json.AbstractSerializerTest

class JotAvailabilitySerializerTest extends AbstractSerializerTest {

    def 'test availability serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VAvailability, new JotAvailabilitySerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a availability is serialized'
        String serialized = mapper.writeValueAsString(availability)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        availability    | expectedSerialized
        availability1   | '{"uid":"123"}'
    }
}
