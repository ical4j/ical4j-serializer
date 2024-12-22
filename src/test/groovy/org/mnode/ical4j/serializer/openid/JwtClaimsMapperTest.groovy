package org.mnode.ical4j.serializer.openid

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.Entity
import spock.lang.Specification

class JwtClaimsMapperTest extends Specification {

    def 'test jwt claims deserialization'() {
        given: 'a jwt string'
        String jwt = '''{
    "name": "John Doe"
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Entity, new JwtClaimsMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the jwt string is deserialized'
        def card = mapper.readValue(jwt, Entity)

        then: 'card matches expected result'
        card as String == '''BEGIN:VCARD\r
NAME:John Doe\r
END:VCARD\r\n'''
    }

}
