package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import spock.lang.Specification

class JCalMapperTest extends Specification {

    def 'test calendar deserialization'() {
        given: 'a json string'
        String json = '["vcalendar",[["prodid",{},"string","-//Ben Fortuna//iCal4j 1.0//EN"],["version",{},"string","2.0"],["uid",{},"string","123"]],[]]'

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new JCalMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is deserialized'
        Calendar calendar = mapper.readValue(json, Calendar)

        then: 'calendar matches expected result'
        calendar as String == 'BEGIN:VCALENDAR\r\nPRODID:-//Ben Fortuna//iCal4j 1.0//EN\r\nVERSION:2.0\r\nUID:123\r\nEND:VCALENDAR\r\n'
    }
}
