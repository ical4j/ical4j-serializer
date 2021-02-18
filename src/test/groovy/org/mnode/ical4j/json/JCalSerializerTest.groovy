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
        serialized == '["vcalendar",[["prodid",{},"text","-//Ben Fortuna//iCal4j 1.0//EN"],["version",{},"text","2.0"],["uid",{},"text","123"]],[["vevent",[["uid",{},"text","1"],["dtstart",{"value":"DATE"},"date-time","20090810"]],[]],["vevent",[["uid",{},"text","2"],["dtstart",{"value":"DATE"},"date-time","20100810"]],[]]]]'
    }
}
