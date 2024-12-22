package org.mnode.ical4j.serializer.jotn

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.Entity
import org.mnode.ical4j.serializer.AbstractSerializerTest

class VCardSerializerTest extends AbstractSerializerTest {

    def 'test card serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Entity, new VCardSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a card is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card    | expectedSerialized
        entity1 | '{"uid":"1","fn":"Test Card"}'
        entity2 | '{"uid":"2","fn":"Jane Doe","adr":";;20341 Whitworth Institute 405 N. Whitworth;Seattle;WA;98052;;"}'
    }
}
