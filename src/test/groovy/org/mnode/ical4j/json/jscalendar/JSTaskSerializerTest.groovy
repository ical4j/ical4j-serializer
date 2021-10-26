package org.mnode.ical4j.json.jscalendar

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VToDo
import org.mnode.ical4j.json.AbstractSerializerTest

class JSTaskSerializerTest extends AbstractSerializerTest {

    def 'test task serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VToDo, new JSTaskSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the task is serialized'
        String serialized = mapper.writeValueAsString(todo)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        todo   | expectedSerialized
        todo1  | '{"@type":"jstask"}'
    }
}
