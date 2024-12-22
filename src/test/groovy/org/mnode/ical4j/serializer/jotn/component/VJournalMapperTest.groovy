package org.mnode.ical4j.serializer.jotn.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VJournal
import org.mnode.ical4j.serializer.jotn.ContentMapper
import spock.lang.Specification

class VJournalMapperTest extends Specification {

    def 'test journal deserialization'() {
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
  "dtstamp": "2019-08-24T14:15:22Z",
  "sequence": 0,
  "recurrence-id": "2019-08-24T14:15:22Z",
  "class": "PUBLIC",
  "status": "DRAFT",
  "rrule": "FREQ=WEEKLY",
  "description": "string",
  "url": "http://example.com",
  "last-modified": "2019-08-24T14:15:22Z",
  "categories": [
    "string"
  ],
  "comment": [
    {
      "text": "string",
      "altrep": "string"
    }
  ],
  "attach": [
    {
      "fmttype": "application/msword",
      "binary": "string"
    }
  ],
  "related-to": {
    "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
    "reltype": "PARENT"
  },
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

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VJournal, new ContentMapper<VJournal>(VJournal::new))
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the journal is deserialized'
        VJournal journal = mapper.readValue(json, VJournal)

        then: 'journal matches expected result'
        journal as String ==~ /BEGIN:VJOURNAL\r
DTSTAMP:\d{8}T\d{6}Z\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ORGANIZER;CN=string;DIR="http:\/\/example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r
SUMMARY:string\r
DTSTAMP:20190824T141522Z\r
SEQUENCE:0\r
RECURRENCE-ID:20190824T141522Z\r
CLASS:PUBLIC\r
STATUS:DRAFT\r
RRULE:FREQ=WEEKLY\r
DESCRIPTION:string\r
URL:http:\/\/example.com\r
LAST-MODIFIED:20190824T141522Z\r
CATEGORIES:string\r
COMMENT;ALTREP=string:string\r
ATTACH;FMTTYPE=application\/msword:string\r
RELATED-TO;RELTYPE=PARENT:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
RDATE:20190824T141522Z\r
EXDATE:20190824T141522Z\r
ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http:\/\/example.com";LANGUAGE=en-US:mailto:joecool@example.com\r
CONTACT;ALTREP=string:string\r
STYLED-DESCRIPTION;FMTTYPE=text\/html;VALUE=TEXT:true\r
STRUCTURED-DATA;VALUE=TEXT;FMTTYPE=application\/xml:string\r
END:VJOURNAL\r\n/
    }
}
