package org.mnode.ical4j.serializer.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAvailability
import spock.lang.Specification

class JotAvailabilityMapperTest extends Specification {

    def 'test availability deserialization'() {
        given: 'a json string'
        String json = '''{
  "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
  "organizer": {
    "cal-address": "mailto:jane_doe@example.com",
    "cn": "string",
    "dir": "http://example.com",
    "sent-by": "mailto:joecool@example.com",
    "language": "en-US"
  },
  "summary": "string",
  "dtstart": "2019-08-24T14:15:22Z",
  "dtend": "2019-08-24T14:15:22Z",
  "sequence": 0,
  "class": "PUBLIC",
  "priority": 0,
  "description": "string",
  "url": "string",
  "location": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "name": "The venue"
  },
  "last-modified": "2019-08-24T14:15:22Z",
  "created": "2019-08-24T14:15:22Z",
  "duration": "PT15M",
  "busytype": "BUSY"
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VAvailability, new JotAvailabilityMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the availability is deserialized'
        VAvailability availability = mapper.readValue(json, VAvailability)

        then: 'availability matches expected result'
        availability as String == '''BEGIN:VAVAILABILITY\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ORGANIZER;CN=string;DIR="http://example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r
SUMMARY:string\r
DTSTART:20190824T141522Z\r
DTEND:20190824T141522Z\r
SEQUENCE:0\r
CLASS:PUBLIC\r
PRIORITY:0\r
DESCRIPTION:string\r
URL:string\r
LOCATION:The venue\r
LAST-MODIFIED:20190824T141522Z\r
CREATED:20190824T141522Z\r
DURATION:PT15M\r
BUSYTYPE:BUSY\r
END:VAVAILABILITY\r\n'''
    }
}
