package org.mnode.ical4j.serializer.jsonld

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VToDo
import org.mnode.ical4j.serializer.AbstractSerializerTest

class ActionJsonLdSerializerTest extends AbstractSerializerTest {

    def 'test action serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VToDo, new ActionJsonLdSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the action is serialized'
        String serialized = mapper.writeValueAsString(todo)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        todo   | expectedSerialized
        todo1  | '{"@context":"https://schema.org","@type":"Action","@id":"123"}'
    }
}
