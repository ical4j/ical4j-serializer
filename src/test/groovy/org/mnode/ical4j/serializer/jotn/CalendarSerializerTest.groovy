package org.mnode.ical4j.serializer.jotn

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import org.mnode.ical4j.serializer.AbstractSerializerTest

class CalendarSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new CalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '{"uid":"123"}'
        calendar2   | '{"uid":"1","image":"https://www.abc.net.au/news/image/8413416-1x1-144x144.png","description":"","source":"https://www.abc.net.au/news/feed/51120/rss.xml","last-modified":"20210304T055223Z","url":"https://www.abc.net.au/news/justin/","name":"Just In"}'
        calendar3   | '{"description":"Globally recognized public holidays","categories":["holidays","global"],"name":"International Public Holidays"}'
    }
}
