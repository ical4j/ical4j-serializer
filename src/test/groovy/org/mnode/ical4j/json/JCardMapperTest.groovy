package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import spock.lang.Specification

class JCardMapperTest extends Specification {

    def 'test card deserialization'() {
        given: 'a json string'
        String json = '["vcard",[["fn",{},"text","Test Card"]]]'

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VCard, new JCardMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the card is deserialized'
        VCard card = mapper.readValue(json, VCard)

        then: 'card matches expected result'
        card as String == 'BEGIN:VCARD\r\nFN:Test Card\r\nEND:VCARD\r\n'
    }
}
