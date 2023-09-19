package org.mnode.ical4j.serializer.model.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VJournal
import org.mnode.ical4j.serializer.AbstractSerializerTest

class VJournalSerializerTest extends AbstractSerializerTest {

    def 'test journal serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VJournal, new VJournalSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a journal is serialized'
        String serialized = mapper.writeValueAsString(journal)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        journal    | expectedSerialized
        journal1   | '{"uid":"123"}'
    }
}
