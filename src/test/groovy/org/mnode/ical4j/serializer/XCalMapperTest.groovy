package org.mnode.ical4j.serializer


import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import net.fortuna.ical4j.model.Calendar
import spock.lang.Specification

class XCalMapperTest extends Specification {

    def 'test calendar XML deserialization'() {
        given: 'a json string'
        String xml = getClass().getResourceAsStream('/samples/jcal/1.xml').text

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Calendar, new XCalMapper())
        XmlMapper mapper = XmlMapper.builder().defaultUseWrapper(true).build()
        mapper.registerModule(module)

        when: 'the calendar is deserialized'
        Calendar calendar = mapper.readValue(xml, Calendar)

        then: 'calendar matches expected result'
        calendar as String == getClass().getResourceAsStream('/samples/jcal/1.ics').text
    }
}
