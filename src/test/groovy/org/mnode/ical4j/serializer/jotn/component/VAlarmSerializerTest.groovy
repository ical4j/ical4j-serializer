package org.mnode.ical4j.serializer.jotn.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAlarm
import org.mnode.ical4j.serializer.AbstractSerializerTest

class VAlarmSerializerTest extends AbstractSerializerTest {

    def 'test alarm serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VAlarm, new VAlarmSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a alarm is serialized'
        String serialized = mapper.writeValueAsString(alarm)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        alarm    | expectedSerialized
        alarm1   | '{"uid":"123"}'
    }
}
