package org.mnode.ical4j.json.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VFreeBusy
import spock.lang.Specification

class JotFreeBusyMapperTest extends Specification {

    def 'test freebusy deserialization'() {
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
  "dtstart": "2019-08-24T14:15:22Z",
  "dtend": "2019-08-24T14:15:22Z",
  "url": "http://www.example.com/calendar/busytime/jsmith.ifb",
  "fbperiods": [
    {
      "start": "2019-08-24T14:15:22Z",
      "end": "2019-08-24T14:15:22Z",
      "fbtype": "BUSY",
      "duration": "PT5H30M"
    }
  ],
  "attendee": {
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
  },
  "comment": [
    {
      "text": "string",
      "altrep": "string"
    }
  ],
  "contact": {
    "text": "string",
    "altrep": "string"
  },
  "styled-description": [
    {
      "fmttype": "text/html",
      "text": "<h1>Test Event</h1><p>An example event description<p>",
      "derived": true
    }
  ]
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VFreeBusy, new JotFreeBusyMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the freebusy is deserialized'
        VFreeBusy freeBusy = mapper.readValue(json, VFreeBusy)

        then: 'freebusy matches expected result'
        freeBusy as String == 'BEGIN:VFREEBUSY\r\n' +
                'UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r\n' +
                'ORGANIZER;CN=string;DIR="http://example.com";SENT-BY="mailto:joecool@example.com";LANGUAGE=en-US:mailto:jane_doe@example.com\r\n' +
                'DTSTART:20181208T000000\r\n' +
                'DTEND:20181208T000000\r\n' +
                'URL:http://www.example.com/calendar/busytime/jsmith.ifb\r\n' +
                'FBPERIODS;FBTYPE=BUSY:PT5H30M\r\n' +
                'ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http://example.com";LANGUAGE=en-US:mailto:joecool@example.com\r\n' +
                'COMMENT;ALTREP=string:string\r\n' +
                'CONTACT;ALTREP=string:string\r\n' +
                'STYLED-DESCRIPTION;FMTTYPE=text/html:true\r\n' +
                'END:VFREEBUSY\r\n'
    }
}
