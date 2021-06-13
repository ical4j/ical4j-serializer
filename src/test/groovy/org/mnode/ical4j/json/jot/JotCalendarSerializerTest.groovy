package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import org.mnode.ical4j.json.AbstractSerializerTest

class JotCalendarSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JotCalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '{"prodid":"-//Ben Fortuna//iCal4j 1.0//EN","version":"2.0","uid":"123"}'
        calendar2   | '{"version":"2.0","prodid":"-//ABC Corporation//NONSGML My Product//EN","uid":"1","name":"Just In","description":"","source":"https://www.abc.net.au/news/feed/51120/rss.xml","url":"https://www.abc.net.au/news/justin/","image":"https://www.abc.net.au/news/image/8413416-1x1-144x144.png","last-modified":"20210304T055223Z"}'
    }
}
