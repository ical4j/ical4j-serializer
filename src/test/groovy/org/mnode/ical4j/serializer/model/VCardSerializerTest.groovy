package org.mnode.ical4j.serializer.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import org.mnode.ical4j.serializer.AbstractSerializerTest

class VCardSerializerTest extends AbstractSerializerTest {

    def 'test card serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VCard, new VCardSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a card is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card    | expectedSerialized
        card1   | '{"uid":"1"}'
        card2   | '{"uid":"2"}'
    }
}
