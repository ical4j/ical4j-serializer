package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaOrganizationSerializerTest extends AbstractSerializerTest {

    def 'test organization serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VCard, new SchemaOrganizationSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the organization is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card   | expectedSerialized
        card1  | '{"@context":"https://schema.org","@type":"Organization","@id":"1","name":"Test Card"}'
        card3  | '{"@context":"https://schema.org","@type":"Organization","@id":"3","name":"Acme Inc.","logo":"http//exampleorg/acme+logopn","address":{"@context":"https://schema.org","@type":"PostalAddress","addressLocality":"Seattle","addressRegion":"WA","postalCode":"98052","streetAddress":"20341 Whitworth Institute 405 N. Whitworth"}}'
    }
}
