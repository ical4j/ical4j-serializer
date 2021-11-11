package org.mnode.ical4j.serializer

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import spock.lang.Specification

class JCalMapperTest extends Specification {

    def 'test calendar deserialization'() {
        given: 'a json string'
        String json = getClass().getResourceAsStream('/samples/jcal/1.json').text

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new JCalMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is deserialized'
        Calendar calendar = mapper.readValue(json, Calendar)

        then: 'calendar matches expected result'
        calendar as String == getClass().getResourceAsStream('/samples/jcal/1.ics').text
    }
}
