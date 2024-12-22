package org.mnode.ical4j.serializer.jsonld

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.Entity
import org.mnode.ical4j.serializer.AbstractSerializerTest

class PlaceJsonLdSerializerTest extends AbstractSerializerTest {

    def 'test place serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Entity, new PlaceJsonLdSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the place is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card   | expectedSerialized
        entity1 | '{"@context":"https://schema.org","@type":"Place","@id":"1","name":"Test Card"}'
    }
}
