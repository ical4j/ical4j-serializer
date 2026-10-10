package org.mnode.ical4j.serializer

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VTimeZone
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

    def 'test calendar round trip with timezone (issue #19)'() {
        given: 'a calendar with a timezone definition and local date-times'
        Calendar calendar = new CalendarBuilder().build(new StringReader('''BEGIN:VCALENDAR
VERSION:2.0
PRODID:-// https://add-to-calendar-pro.com // button v2.5.10 //EN
CALSCALE:GREGORIAN
METHOD:PUBLISH
BEGIN:VTIMEZONE
TZID:Europe/Berlin
X-LIC-LOCATION:Europe/Berlin
LAST-MODIFIED:20240205T192834Z
BEGIN:DAYLIGHT
TZNAME:CEST
TZOFFSETFROM:+0100
TZOFFSETTO:+0200
DTSTART:19700329T020000
RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=-1SU
END:DAYLIGHT
BEGIN:STANDARD
TZNAME:CET
TZOFFSETFROM:+0200
TZOFFSETTO:+0100
DTSTART:19701025T030000
RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU
END:STANDARD
END:VTIMEZONE
BEGIN:VEVENT
UID:3c9de19f-1dec-4507-a41b-4213d270f359
DTSTAMP:20240220T065249Z
DTSTART;TZID=Europe/Berlin:20240209T140000
DTEND;TZID=Europe/Berlin:20240209T220000
SUMMARY:Day Shift
DESCRIPTION:Day shift
RRULE:FREQ=WEEKLY;WKST=MO;INTERVAL=1;BYDAY=MO,TU,WE,TH,FR
TRANSP:OPAQUE
SEQUENCE:0
STATUS:CONFIRMED
CREATED:20240220T065213Z
LAST-MODIFIED:20240220T065213Z
END:VEVENT
END:VCALENDAR
'''.replace('\n', '\r\n')))

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JCalSerializer(Calendar))
        module.addDeserializer(Calendar, new JCalMapper(Calendar))
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized and deserialized'
        String json = mapper.writeValueAsString(calendar)
        Calendar deserialized = mapper.readValue(json, Calendar)

        then: 'the calendar structure is preserved'
        deserialized.getComponents().collect { it.class } == [VTimeZone, VEvent]
        deserialized.getComponent('VTIMEZONE').get().getComponents()*.name == ['DAYLIGHT', 'STANDARD']

        and: 'local date-times and timezone references are preserved'
        def event = deserialized.getComponent('VEVENT').get()
        event.getProperty('DTSTART').get().getParameter('TZID').get().value == 'Europe/Berlin'
        event.getProperty('DTSTART').get().value == '20240209T140000'
        event.getProperty('DTSTAMP').get().value == '20240220T065249Z'

        and: 're-serializing produces the same jCal'
        mapper.writeValueAsString(deserialized) == json
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
