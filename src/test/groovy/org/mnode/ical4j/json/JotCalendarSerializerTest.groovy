package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar

class JotCalendarSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JotCalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar1)

        then: 'serialized string matches expected value'
        serialized == '{"id":"123"}'
    }
}
