package org.mnode.ical4j.json.jscalendar

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import org.mnode.ical4j.json.AbstractSerializerTest
import org.mnode.ical4j.json.jscalendar.JSGroupSerializer

class JSGroupSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JSGroupSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '{"@type":"jsgroup","uid":"123"}'
        calendar2   | '{"@type":"jsgroup","uid":"1"}'
    }
}
