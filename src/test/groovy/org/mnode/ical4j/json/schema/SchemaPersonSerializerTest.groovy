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
        card1  | '{"@context":"https://schema.org","@type":"Person","@id":"1","name":"Test Card"}'
        card2  | '{"@context":"https://schema.org","@type":"Person","@id":"2","name":"Jane Doe","address":{"@context":"https://schema.org","@type":"PostalAddress","addressLocality":"Seattle","addressRegion":"WA","postalCode":"98052","streetAddress":"20341 Whitworth Institute 405 N. Whitworth"}}'
    }
}
