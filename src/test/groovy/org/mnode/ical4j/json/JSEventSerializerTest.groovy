package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Attach

class JSEventSerializerTest extends AbstractSerializerTest {

    def 'test event serialization'() {
        given: 'a calendar'
        ContentBuilder builder = []
        VEvent event = builder.vevent() {
            uid '1'
            dtstamp()
            dtstart '20090810', parameters: parameters { value 'DATE' }
            action 'DISPLAY'
            attach new Attach(new File('gradle/wrapper/gradle-wrapper.jar').bytes)
        }

        and: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VEvent, new JSEventSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the event is serialized'
        String serialized = mapper.writeValueAsString(event)

        then: 'serialized string matches expected value'
        serialized == '{"@type":"jsevent"}'
    }
}
