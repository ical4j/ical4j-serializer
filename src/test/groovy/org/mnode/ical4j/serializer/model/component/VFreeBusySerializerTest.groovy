package org.mnode.ical4j.serializer.model.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VFreeBusy
import org.mnode.ical4j.serializer.AbstractSerializerTest

class VFreeBusySerializerTest extends AbstractSerializerTest {

    def 'test freebusy serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VFreeBusy, new VFreeBusySerializer())
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
