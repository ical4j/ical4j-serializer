package org.mnode.ical4j.serializer.model.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.Available
import org.mnode.ical4j.serializer.AbstractSerializerTest

class AvailableSerializerTest extends AbstractSerializerTest {

    def 'test available serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Available, new AvailableSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a available is serialized'
        String serialized = mapper.writeValueAsString(available)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        available    | expectedSerialized
        available1   | '{"uid":"123"}'
    }
}
