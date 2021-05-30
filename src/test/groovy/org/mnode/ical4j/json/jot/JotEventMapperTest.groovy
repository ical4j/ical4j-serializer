package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class JotEventMapperTest extends Specification {

    def 'test event deserialization'() {
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
  "sequence": 0,
  "recurrence-id": "2019-08-24T14:15:22Z",
  "class": "PUBLIC",
  "priority": 0,
  "status": "TENTATIVE",
  "dtend": "2019-08-24T14:15:22Z",
  "duration": "PT15M",
  "rrule": "FREQ=WEEKLY",
  "description": "string",
  "url": "http://example.com",
  "geo": "49.8932;40.3834",
  "location": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "name": "The venue"
  },
  "last-modified": "2019-08-24T14:15:22Z",
  "created": "2019-08-24T14:15:22Z",
  "categories": [
    "string"
  ],
  "comment": [
    {
      "text": "string",
      "altrep": "string"
    }
  ],
  "resources": [
    {
      "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
      "altrep": "CID:part3.msg.970415T083000@example.com"
    }
  ],
  "attach": [
    {
      "fmttype": "string",
      "url": "http://example.com"
    }
  ],
  "related-to": [
    {
      "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
      "reltype": "PARENT"
    }
  ],
  "rdate": [
    "2019-08-24T14:15:22Z"
  ],
  "exdate": [
    "2019-08-24T14:15:22Z"
  ],
  "attendee": [
    {
      "cal-address": "mailto:joecool@example.com",
      "cu-type": "INDIVIDUAL",
      "member": "mailto:DEV-GROUP@example.com",
      "role": "REQ-PARTICIPANT",
      "partstat": "NEEDS-ACTION",
      "rsvp": false,
      "delto": "mailto:joecool@example.com",
      "delfrom": "mailto:joecool@example.com",
      "sentby": "mailto:joecool@example.com",
      "cn": "string",
      "dir": "http://example.com",
      "language": "en-US"
    }
  ],
  "transp": "OPAQUE",
  "contact": [
    {
      "text": "string",
      "altrep": "string"
    }
  ],
  "styled-description": [
    {
      "fmttype": "text/html",
      "text": "<h1>Test Event</h1><p>An example event description<p>",
      "derived": true
    }
  ],
  "structured-data": [
    {
      "fmttype": "string",
      "schema": "string",
      "encoding": "BASE64",
      "text": "string"
    }
  ]
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VEvent, new JotEventMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the event is deserialized'
        VEvent event = mapper.readValue(json, VEvent)

        then: 'event matches expected result'
        event as String == 'BEGIN:VEVENT\r\n' +
                'UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r\n' +
                'ORGANIZER;LANGUAGE=en-US:mailto:joecool@example.com\r\n' +
                'SUMMARY:string\r\n' +
                'DTSTART:20181208T000000\r\n' +
                'SEQUENCE:0\r\n' +
                'RECURRENCE-ID:20181208T000000\r\n' +
                'CLASS:PUBLIC\r\n' +
                'PRIORITY:0\r\n' +
                'STATUS:TENTATIVE\r\n' +
                'DTEND:20181208T000000\r\n' +
                'DURATION:PT15M\r\n' +
                'RRULE:FREQ=WEEKLY\r\n' +
                'DESCRIPTION:string\r\n' +
                'URL:http://example.com\r\n' +
                'GEO:49.8932;40.3834\r\n' +
                'LOCATION:The venue\r\n' +
                'LAST-MODIFIED:20181208T000000\r\n' +
                'CREATED:20181208T000000\r\n' +
                'CATEGORIES:string\r\n' +
                'COMMENT;ALTREP=string:string\r\n' +
                'RESOURCES;ALTREP="CID:part3.msg.970415T083000@example.com":07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r\n' +
                'ATTACH;FMTTYPE=string:http://example.com\r\n' +
                'RELATED-TO:PARENT\r\n' +
                'RDATE:20181208T000000\r\n' +
                'EXDATE:20181208T000000\r\n' +
                'ATTENDEE;LANGUAGE=en-US:http://example.com\r\n' +
                'TRANSP:OPAQUE\r\n' +
                'CONTACT;ALTREP=string:string\r\n' +
                'STYLED-DESCRIPTION;FMTTYPE=text/html:true\r\n' +
                'STRUCTURED-DATA;FMTTYPE=string:string\r\n' +
                'END:VEVENT\r\n'
    }
}
