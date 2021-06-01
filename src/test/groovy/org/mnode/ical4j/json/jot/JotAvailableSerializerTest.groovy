package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.Available
import org.mnode.ical4j.json.AbstractSerializerTest

class JotAvailableSerializerTest extends AbstractSerializerTest {

    def 'test available serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Available, new JotAvailableSerializer())
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
