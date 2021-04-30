package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard

class JCardSerializerTest extends AbstractSerializerTest {

    def 'test card serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VCard, new JCardSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the card is serialized'
        String serialized = mapper.writeValueAsString(card)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        card    | expectedSerialized
        card1   | '["vcard",[["fn",{},"text","Test Card"]]]'
    }
}
