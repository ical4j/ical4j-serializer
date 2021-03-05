package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.util.Calendars
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

    def 'test calendar serialization 2'() {
        given: 'a calendar'
        Calendar calendar = Calendars.load(file)

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JCalSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == '["vcalendar",[["version",{},"text","2.0"],["prodid",{},"text","-//ABC Corporation//NONSGML My Product//EN"],["uid",{},"text","1"],["name",{},"text","Just In"],["description",{},"text",""],["source",{},"uri","https://www.abc.net.au/news/feed/51120/rss.xml"],["url",{},"uri","https://www.abc.net.au/news/justin/"],["image",{},"uri","https://www.abc.net.au/news/image/8413416-1x1-144x144.png"],["last-modified",{},"date-time","20210304T055223Z"]],[["vjournal",[["uid",{},"text","https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856"],["summary",{},"text","The Gold Coast needs 6,500 new homes a year, but where can they be built?"],["description",{},"text","\\n              \\n              <p>The famous coastal city is fast running out of greenfield land to house its growing population, but the community is opposed to higher-density developments in the city.</p>\\n              \\n"],["categories",{},"text","Housing Industry,Rental Housing,Housing,Agribusiness"],["dtstamp",{},"date-time","20210304T055223Z"],["url",{},"uri","https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856"],["contact",{},"text","Dominic Cansdale"],["image",{},"uri","https://www.abc.net.au/news/image/12721466-3x2-940x627.jpg"]],[]]]]'

        where:
        file << [
                'src/test/resources/samples/justin.ics'
        ]
    }
}
