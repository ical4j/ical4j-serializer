package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VToDo
import org.mnode.ical4j.json.AbstractSerializerTest

class JotToDoSerializerTest extends AbstractSerializerTest {

    def 'test todo serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VToDo, new JotToDoSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a todo is serialized'
        String serialized = mapper.writeValueAsString(todo)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        todo    | expectedSerialized
        todo1   | '{"id":"123"}'
    }
}
