package org.mnode.ical4j.json.schema

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VJournal
import org.mnode.ical4j.json.AbstractSerializerTest

class SchemaCreativeWorkSerializerTest extends AbstractSerializerTest {

    def 'test creative work serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VJournal, new SchemaCreativeWorkSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the creative work is serialized'
        String serialized = mapper.writeValueAsString(journal)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        journal   | expectedSerialized
        journal1  | '{"@context":"https://schema.org","@type":"CreativeWork","@id":"123"}'
    }
}
