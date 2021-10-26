package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaPlaceSerializerTest extends AbstractSerializerTest {

    def 'test place serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VCard, new SchemaPlaceSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the place is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card   | expectedSerialized
        card1  | '{"@context":"https://schema.org","@type":"Place","@id":"1","name":"Test Card"}'
    }
}
