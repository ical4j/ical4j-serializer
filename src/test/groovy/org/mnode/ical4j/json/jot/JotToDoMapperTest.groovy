package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VToDo
import spock.lang.Specification

class JotToDoMapperTest extends Specification {

    def 'test todo deserialization'() {
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
  "status": "NEEDS-ACTION",
  "due": "2019-08-24T14:15:22Z",
  "duration": "PT15M",
  "rrule": "FREQ=WEEKLY",
  "url": "http://example.com",
  "geo": "49.8932;40.3834",
  "location": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "name": "The venue"
  },
  "last-modified": "2019-08-24T14:15:22Z",
  "created": "2019-08-24T14:15:22Z",
  "percent-complete": 0,
  "description": "string",
  "categories": [
    "string"
  ],
  "comments": [
    "string"
  ],
  "resources": [
    {
      "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
      "altrep": "CID:part3.msg.970415T083000@example.com"
    }
  ],
  "attachments": [
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
  "rdates": [
    "string"
  ],
  "exdates": [
    "string"
  ],
  "attendees": [
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
  "contacts": [
    "string"
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
        module.addDeserializer(VToDo, new JotToDoMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the todo is deserialized'
        VToDo toDo = mapper.readValue(json, VToDo)

        then: 'todo matches expected result'
        toDo as String == '''BEGIN:VTODO\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ORGANIZER;CN=string;DIR="http://example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r
SUMMARY:string\r
DTSTART:20181208T000000\r
SEQUENCE:0\r
RECURRENCE-ID:20181208T000000\r
CLASS:PUBLIC\r
PRIORITY:0\r
STATUS:NEEDS-ACTION\r
DUE:20181208T000000\r
DURATION:PT15M\r
RRULE:FREQ=WEEKLY\r
URL:http://example.com\r
GEO:49.8932;40.3834\r
LOCATION:The venue\r
LAST-MODIFIED:20181208T000000\r
CREATED:20181208T000000\r
PERCENT-COMPLETE:0\r
DESCRIPTION:string\r
CATEGORIES:string\r
COMMENTS:string\r
RESOURCES;ALTREP="CID:part3.msg.970415T083000@example.com":07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ATTACHMENTS;FMTTYPE=string:http://example.com\r
RELATED-TO;RELTYPE=PARENT:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
RDATES:string\r
EXDATES:string\r
ATTENDEES;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http://example.com";LANGUAGE=en-US:mailto:joecool@example.com\r
CONTACTS:string\r
STYLED-DESCRIPTION;FMTTYPE=text/html;VALUE=TEXT:true\r
STRUCTURED-DATA;FMTTYPE=string:string\r
END:VTODO\r\n'''
    }
}
