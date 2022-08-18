package org.mnode.ical4j.serializer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import spock.lang.Ignore
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

    @Ignore('the unique approach of jCal in using array as object structure seems to affect ability to parse array of objects')
    def 'test list deserialization'() {
        given: 'a json string'
        String json = getClass().getResourceAsStream('/samples/jcal/list.json').text

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new JCalMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar list is deserialized'
        List<Calendar> calendars = mapper.readValue(json, new TypeReference<List<Calendar>>() {})

        then: 'calendar list length matches expected result'
        calendars.size() == 2
    }

    @Ignore('the unique approach of jCal in using array as object structure seems to affect ability to parse array of objects')
    def 'test array deserialization'() {
        given: 'a json string'
        String json = getClass().getResourceAsStream('/samples/jcal/list.json').text

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new JCalMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar array is deserialized'
        Calendar[] calendars = mapper.readValue(json, Calendar[])

        then: 'calendar array length matches expected result'
        calendars.length == 2
    }
}
