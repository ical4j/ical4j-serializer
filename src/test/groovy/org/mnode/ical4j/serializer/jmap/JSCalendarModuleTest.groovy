package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.util.Calendars
import net.fortuna.ical4j.vcard.VCardBuilder
import spock.lang.Specification
import spock.lang.Unroll

class JSCalendarModuleTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JSCalendarModule())

    @Unroll
    def 'event fixture #name round-trips to golden JSON'() {
        given:
        def calendar = Calendars.load("src/test/resources/samples/jscalendar/${name}.ics")
        def event = calendar.getComponent('VEVENT').get()
        JsonNode expected = mapper.readTree(new File("src/test/resources/samples/jscalendar/${name}.json"))

        when:
        JsonNode actual = mapper.valueToTree(event)

        then:
        actual == expected

        where:
        name << ['event-utc', 'event-zoned', 'event-allday', 'event-recurring']
    }

    def 'task fixture serializes to golden JSON'() {
        given:
        def calendar = Calendars.load('src/test/resources/samples/jscalendar/task-due.ics')
        def todo = calendar.getComponent('VTODO').get()
        JsonNode expected = mapper.readTree(new File('src/test/resources/samples/jscalendar/task-due.json'))

        when:
        JsonNode actual = mapper.valueToTree(todo)

        then:
        actual == expected
    }

    def 'card fixture serializes to golden JSON'() {
        given:
        def vcard = new VCardBuilder(new File('src/test/resources/samples/jscalendar/card-basic.vcf').newReader()).build()
        def entity = vcard.entityList.all.first()
        JsonNode expected = mapper.readTree(new File('src/test/resources/samples/jscalendar/card-basic.json'))

        when:
        JsonNode actual = mapper.valueToTree(entity)

        then:
        actual == expected
    }
}
