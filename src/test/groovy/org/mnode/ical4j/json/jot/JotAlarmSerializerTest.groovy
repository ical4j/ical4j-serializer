package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAlarm
import org.mnode.ical4j.json.AbstractSerializerTest

class JotAlarmSerializerTest extends AbstractSerializerTest {

    def 'test alarm serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(VAlarm, new JotAlarmSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'a alarm is serialized'
        String serialized = mapper.writeValueAsString(alarm)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        alarm    | expectedSerialized
        alarm1   | '{"id":"123"}'
    }
}
