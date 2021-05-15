package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaPersonSerializerTest extends AbstractSerializerTest {

    def 'test person serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VCard, new SchemaPersonSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the person is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card   | expectedSerialized
        card1  | '{"@context":"https://schema.org","@type":"Person","@id":"1"}'
    }
}
