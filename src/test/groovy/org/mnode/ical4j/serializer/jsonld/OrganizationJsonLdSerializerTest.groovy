package org.mnode.ical4j.serializer.jsonld

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.Entity
import org.mnode.ical4j.serializer.AbstractSerializerTest

class OrganizationJsonLdSerializerTest extends AbstractSerializerTest {

    def 'test organization serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Entity, new OrganizationJsonLdSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the organization is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card   | expectedSerialized
        entity1 | '{"@context":"https://schema.org","@type":"Organization","@id":"1","name":"Test Card"}'
        entity3 | '{"@context":"https://schema.org","@type":"Organization","@id":"3","name":"Acme Inc.","logo":"http//exampleorg/acme+logopn","address":{"@context":"https://schema.org","@type":"PostalAddress","addressLocality":"Seattle","addressRegion":"WA","postalCode":"98052","streetAddress":"20341 Whitworth Institute 405 N. Whitworth"}}'
    }
}
