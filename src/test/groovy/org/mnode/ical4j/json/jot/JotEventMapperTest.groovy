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
  "class": "PUBLIC",
  "priority": 0,
  "status": "TENTATIVE",
  "dtend": "2019-08-24T14:15:22Z",
  "duration": "PT15M",
  "description": "string",
  "url": "http://example.com",
  "location": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "name": "The venue"
  },
  "last-modified": "2019-08-24T14:15:22Z",
  "created": "2019-08-24T14:15:22Z",
  "categories": [
    "string"
  ],
  "comments": [
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
    "2019-08-24T14:15:22Z"
  ],
  "exdates": [
    "2019-08-24T14:15:22Z"
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
  "transp": "OPAQUE",
  "contacts": [
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
                'DTSTAMP:20210530T023910Z\r\n' +
                'UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r\n' +
                'ORGANIZER;LANGUAGE=en-US:mailto:joecool@example.com\r\n' +
                'SUMMARY:string\r\n' +
                'DTSTART:20181208T000000\r\n' +
                'SEQUENCE:0\r\n' +
                'CLASS:PUBLIC\r\n' +
                'PRIORITY:0\r\n' +
                'STATUS:TENTATIVE\r\n' +
                'DTEND:20181208T000000\r\n' +
                'DURATION:PT15M\r\n' +
                'DESCRIPTION:string\r\n' +
                'URL:http://example.com\r\n' +
                'LOCATION:The venue\r\n' +
                'LAST-MODIFIED:20181208T000000\r\n' +
                'CREATED:20181208T000000\r\n' +
                'CATEGORIES:string\r\n' +
                'COMMENTS;ALTREP=string:string\r\n' +
                'RESOURCES;ALTREP="CID:part3.msg.970415T083000@example.com":07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r\n' +
                'ATTACHMENTS;FMTTYPE=string:http://example.com\r\n' +
                'RELATED-TO:PARENT\r\n' +
                'RDATES:2019-08-24T14:15:22Z\r\n' +
                'EXDATES:2019-08-24T14:15:22Z\r\n' +
                'ATTENDEES;LANGUAGE=en-US:http://example.com\r\n' +
                'TRANSP:OPAQUE\r\n' +
                'CONTACTS;ALTREP=string:string\r\n' +
                'STYLED-DESCRIPTION;FMTTYPE=text/html:true\r\n' +
                'STRUCTURED-DATA;FMTTYPE=string:string\r\n' +
                'END:VEVENT\r\n'
    }
}
