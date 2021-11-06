package org.mnode.ical4j.serializer.jot

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
      "derived": true,
      "value": "TEXT"
    }
  ],
  "structured-data": [
    {
      "fmttype": "string",
      "schema": "string",
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
        event as String == '''BEGIN:VEVENT\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ORGANIZER;CN=string;DIR="http://example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r
SUMMARY:string\r
DTSTART:20181208T000000\r
SEQUENCE:0\r
RECURRENCE-ID:20181208T000000\r
CLASS:PUBLIC\r
PRIORITY:0\r
STATUS:TENTATIVE\r
DTEND:20181208T000000\r
DURATION:PT15M\r
RRULE:FREQ=WEEKLY\r
DESCRIPTION:string\r
URL:http://example.com\r
GEO:49.8932;40.3834\r
LOCATION:The venue\r
LAST-MODIFIED:20181208T000000\r
CREATED:20181208T000000\r
CATEGORIES:string\r
COMMENT;ALTREP=string:string\r
RESOURCES;ALTREP="CID:part3.msg.970415T083000@example.com":07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ATTACH;FMTTYPE=string:http://example.com\r
RELATED-TO;RELTYPE=PARENT:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
RDATE:20181208T000000\r
EXDATE:20181208T000000\r
ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http://example.com";LANGUAGE=en-US:mailto:joecool@example.com\r
TRANSP:OPAQUE\r
CONTACT;ALTREP=string:string\r
STYLED-DESCRIPTION;FMTTYPE=text/html;VALUE=TEXT:true\r
STRUCTURED-DATA;FMTTYPE=string:string\r
END:VEVENT\r\n'''
    }
}
