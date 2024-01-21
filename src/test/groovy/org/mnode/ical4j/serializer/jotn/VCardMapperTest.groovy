package org.mnode.ical4j.serializer.jotn

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.VCard
import spock.lang.Specification

class VCardMapperTest extends Specification {

    def 'test card deserialization'() {
        given: 'a json string'
        String json = '''{
  "kind": "individual",
  "n": ";;;;;;",
  "categories": "contacts"
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VCard, new VCardMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the card is deserialized'
        VCard card = mapper.readValue(json, VCard)

        then: 'card matches expected result'
        card as String == '''BEGIN:VCARD\r
KIND:individual\r
N:;;;;\r
CATEGORIES:contacts\r
END:VCARD\r\n'''
    }
}
