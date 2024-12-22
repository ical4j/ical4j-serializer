package org.mnode.ical4j.serializer.jotn.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.Available
import org.mnode.ical4j.serializer.jotn.ContentMapper
import spock.lang.Specification

class AvailableMapperTest extends Specification {

    def 'test available deserialization'() {
        given: 'a json string'
        String json = '''{
  "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
  "dtstart": "2019-08-24T14:15:22Z",
  "dtend": "2019-08-24T14:15:22Z",
  "duration": "PT15M",
  "summary": "string",
  "recurrence-id": "2019-08-24T14:15:22Z",
  "location": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "name": "The venue"
  },
  "description": "string",
  "created": "2019-08-24T14:15:22Z",
  "last-modified": "2019-08-24T14:15:22Z",
  "rrule": "FREQ=WEEKLY",
  "categories": [
    "string"
  ],
  "comments": [
    "string"
  ],
  "exdate": [
    "2019-08-24T14:15:22Z"
  ],
  "rdate": [
    "2019-08-24T14:15:22Z"
  ],
  "contacts": [
    "string"
  ]
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(Available, new ContentMapper<Available>(Available::new))
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the available is deserialized'
        Available available = mapper.readValue(json, Available)

        then: 'available matches expected result'
        available as String == '''BEGIN:AVAILABLE\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
DTSTART:20190824T141522Z\r
DTEND:20190824T141522Z\r
DURATION:PT15M\r
SUMMARY:string\r
RECURRENCE-ID:20190824T141522Z\r
LOCATION:The venue\r
DESCRIPTION:string\r
CREATED:20190824T141522Z\r
LAST-MODIFIED:20190824T141522Z\r
RRULE:FREQ=WEEKLY\r
CATEGORIES:string\r
COMMENTS:string\r
EXDATE:20190824T141522Z\r
RDATE:20190824T141522Z\r
CONTACTS:string\r
END:AVAILABLE\r\n'''
    }
}
