package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.property.Attach
import spock.lang.Specification

class JSCalendarSerializerTest extends Specification {

    def 'test calendar serialization'() {
        given: 'a calendar'
        ContentBuilder builder = []
        Calendar calendar = builder.calendar() {
            prodid '-//Ben Fortuna//iCal4j 1.0//EN'
            version '2.0'
            uid '123'
            vevent {
                uid '1'
                dtstamp()
                dtstart '20090810', parameters: parameters { value 'DATE' }
            }
            vevent {
                uid '2'
                dtstamp()
                dtstart '20100810', parameters: parameters { value 'DATE' }
            }
        }

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JSCalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == '{"@type":"jsgroup","uid":"123"}'
    }

    def 'test event serialization'() {
        given: 'a calendar'
        ContentBuilder builder = []
        Calendar calendar = builder.calendar() {
            prodid '-//Ben Fortuna//iCal4j 1.0//EN'
            version '2.0'
            vevent {
                uid '1'
                dtstamp()
                dtstart '20090810', parameters: parameters { value 'DATE' }
                action 'DISPLAY'
                attach new Attach(new File('gradle/wrapper/gradle-wrapper.jar').bytes)
            }
        }

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JSCalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == '{"@type":"jsevent"}'
    }
}
