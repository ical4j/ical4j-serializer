package org.mnode.ical4j.serializer.jotn.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VEvent
import org.mnode.ical4j.serializer.jotn.ContentMapper
import spock.lang.Shared
import spock.lang.Specification

class VEventMapperTest extends Specification {

    @Shared
    ObjectMapper mapper

    def setupSpec() {
        SimpleModule module = []
        module.addDeserializer(VEvent, new ContentMapper<VEvent>(VEvent::new))
        mapper = []
        mapper.registerModule(module)
    }

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
      "fmttype": "application/msword",
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
      "value": "text",
      "fmttype": "application/xml",
      "schema": "string",
      "text": "string"
    }
  ]
}'''

        when: 'the event is deserialized'
        VEvent event = mapper.readValue(json, VEvent)

        then: 'event matches expected result'
        event as String ==~ /BEGIN:VEVENT\r
DTSTAMP:\d{8}T\d{6}Z\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ORGANIZER;CN=string;DIR="http:\/\/example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r
SUMMARY:string\r
DTSTART:20190824T141522Z\r
SEQUENCE:0\r
RECURRENCE-ID:20190824T141522Z\r
CLASS:PUBLIC\r
PRIORITY:0\r
STATUS:TENTATIVE\r
DTEND:20190824T141522Z\r
DURATION:PT15M\r
RRULE:FREQ=WEEKLY\r
DESCRIPTION:string\r
URL:http:\/\/example.com\r
GEO:49.8932;40.3834\r
LOCATION:The venue\r
LAST-MODIFIED:20190824T141522Z\r
CREATED:20190824T141522Z\r
CATEGORIES:string\r
COMMENT;ALTREP=string:string\r
RESOURCES;ALTREP="CID:part3.msg.970415T083000@example.com":07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ATTACH;FMTTYPE=application\/msword:http:\/\/example.com\r
RELATED-TO;RELTYPE=PARENT:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
RDATE:20190824T141522Z\r
EXDATE:20190824T141522Z\r
ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http:\/\/example.com";LANGUAGE=en-US:mailto:joecool@example.com\r
TRANSP:OPAQUE\r
CONTACT;ALTREP=string:string\r
STYLED-DESCRIPTION;FMTTYPE=text\/html;VALUE=TEXT:true\r
STRUCTURED-DATA;VALUE=TEXT;FMTTYPE=application\/xml:string\r
END:VEVENT\r\n/
    }

    def 'test deserialization with property array'() {
        given: 'a json string'
        String json = '''{
  "dtstart": "2024-01-01",
  "summary": "New Years Day",
  "concept": "ical4j:concept:observance:observance",
  "categories": ["holidays", "international,global"]
}'''
        when: 'the event is deserialized'
        VEvent event = mapper.readValue(json, VEvent)

        then: 'event matches expected result'
        event as String ==~ /BEGIN:VEVENT\r
DTSTAMP:\d{8}T\d{6}Z\r
DTSTART:20240101\r
SUMMARY:New Years Day\r
CONCEPT:ical4j:concept:observance:observance\r
CATEGORIES:holidays\r
CATEGORIES:international,global\r
END:VEVENT\r\n/
    }

    def 'test template deserialization'() {
        given: 'a json string'
        String json = '''{
"concept": "ical4j:concept:event:meeting",
"dtstart": "2023-11-24T09:30:00Z",
"dtend": "2023-11-24T10:30:00Z",
"summary": "Sample meeting"
}
'''
        when: 'the event is deserialized'
        VEvent event = mapper.readValue(json, VEvent)

        then: 'event matches expected result'
        event as String ==~ /BEGIN:VEVENT\r
DTSTAMP:\d{8}T\d{6}Z\r
CONCEPT:ical4j:concept:event:meeting\r
DTSTART:20231124T093000Z\r
DTEND:20231124T103000Z\r
SUMMARY:Sample meeting\r
END:VEVENT\r\n/
    }
}
