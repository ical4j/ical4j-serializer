package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar

class JSGroupSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JSGroupSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar1)

        then: 'serialized string matches expected value'
        serialized == '{"@type":"jsgroup","uid":"123"}'
    }
}
