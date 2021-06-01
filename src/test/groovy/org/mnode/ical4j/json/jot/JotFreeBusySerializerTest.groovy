package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VFreeBusy
import org.mnode.ical4j.json.AbstractSerializerTest

class JotFreeBusySerializerTest extends AbstractSerializerTest {

    def 'test freebusy serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VFreeBusy, new JotFreeBusySerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a freebusy is serialized'
        String serialized = mapper.writeValueAsString(freebusy)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        freebusy    | expectedSerialized
        freebusy1   | '{"uid":"123"}'
    }
}
