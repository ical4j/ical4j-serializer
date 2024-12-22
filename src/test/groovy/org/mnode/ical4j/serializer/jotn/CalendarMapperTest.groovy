package org.mnode.ical4j.serializer.jotn

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import spock.lang.Specification

class CalendarMapperTest extends Specification {

    def 'test calendar deserialization'() {
        given: 'a json string'
        String json = '''{
  "uid": "497f6eca-6276-4993-bfeb-53cbbbba6f08",
  "calscale": "GREGORIAN",
  "name": "My Social Calendar ",
  "description": "A collection of social events, journals, tasks and availability",
  "last-modified": "2019-08-24T14:15:22Z",
  "url": "https://calendar.example.com/mysocialcalendar.html",
  "categories": [
    "social"
  ],
  "refresh-interval": "P1W",
  "source": "https://calendar.example.com/mysocialcalendar.ics",
  "color": "red",
  "image": "https://calendar.example.com/mysocialcalendar.png",
  "conference": "https://meeting.example.com/mysocialcalendar"
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new ContentMapper<Calendar>(Calendar::new))
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is deserialized'
        Calendar calendar = mapper.readValue(json, Calendar)

        then: 'calendar matches expected result'
        calendar as String == 'BEGIN:VCALENDAR\r\n' +
                'UID:497f6eca-6276-4993-bfeb-53cbbbba6f08\r\n' +
                'CALSCALE:GREGORIAN\r\n' +
                'NAME:My Social Calendar\r\n' +
                'DESCRIPTION:A collection of social events\\, journals\\, tasks and availability\r\n' +
                'LAST-MODIFIED:20190824T141522Z\r\n' +
                'URL:https://calendar.example.com/mysocialcalendar.html\r\n' +
                'CATEGORIES:social\r\n' +
                'REFRESH-INTERVAL:P1W\r\n' +
                'SOURCE:https://calendar.example.com/mysocialcalendar.ics\r\n' +
                'COLOR:red\r\n' +
                'IMAGE:https://calendar.example.com/mysocialcalendar.png\r\n' +
                'CONFERENCE:https://meeting.example.com/mysocialcalendar\r\n' +
                'END:VCALENDAR\r\n'
    }
}
