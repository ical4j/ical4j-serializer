package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import spock.lang.Specification

class JCalSerializerTest extends Specification {

    def 'test calendar serialization'() {
        given: 'a calendar'
        ContentBuilder builder = []
        Calendar calendar = builder.calendar() {
            prodid '-//Ben Fortuna//iCal4j 1.0//EN'
            version '2.0'
            uid '123'
            vevent {
                uid '1'
                dtstart '20090810', parameters: parameters { value 'DATE' }
            }
            vevent {
                uid '2'
                dtstart '20100810', parameters: parameters { value 'DATE' }
            }
        }

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JCalSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == '["vcalendar",[["prodid",{},"string","-//Ben Fortuna//iCal4j 1.0//EN"],["version",{},"string","2.0"],["uid",{},"string","123"]],[["VEVENT",[["uid",{},"string","1"],["dtstart",{"value":"DATE"},"date","20090810"]],[]],["VEVENT",[["uid",{},"string","2"],["dtstart",{"value":"DATE"},"date","20100810"]],[]]]]'
    }
}
