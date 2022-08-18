package org.mnode.ical4j.serializer.jot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.component.VAlarm
import spock.lang.Specification

class JotAlarmMapperTest extends Specification {

    def 'test alarm deserialization'() {
        given: 'a json string'
        String json = '''{
  "uid": "07cc67f4-45d6-494b-adac-09b5cbc7e2b5",
  "action": "AUDIO",
  "trigger": {
    "dtstart": "2019-08-24T14:15:22Z"
  },
  "description": "string",
  "duration": "PT15M",
  "repeat": 1,
  "summary": "string",
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
  "attachments": [
    {
      "fmttype": "string",
      "url": "http://example.com"
    }
  ],
  "styled-description": [
    {
      "fmttype": "text/html",
      "text": "<h1>Test Event</h1><p>An example event description<p>",
      "derived": true,
      "value": "TEXT"
    }
  ]
}'''

        and: 'an object mapper'
        SimpleModule module = []
        module.addDeserializer(VAlarm, new JotAlarmMapper())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the alarm is deserialized'
        VAlarm alarm = mapper.readValue(json, VAlarm)

        then: 'alarm matches expected result'
        alarm as String == '''BEGIN:VALARM\r
UID:07cc67f4-45d6-494b-adac-09b5cbc7e2b5\r
ACTION:AUDIO\r
TRIGGER:20190824T141522Z\r
DESCRIPTION:string\r
DURATION:PT15M\r
REPEAT:1\r
SUMMARY:string\r
ATTENDEES;MEMBER="mailto:DEV-GROUP@example.com";ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=FALSE;CN=string;DIR="http://example.com";LANGUAGE=en-US:mailto:joecool@example.com\r
ATTACHMENTS;FMTTYPE=string:http://example.com\r
STYLED-DESCRIPTION;FMTTYPE=text/html;VALUE=TEXT:true\r
END:VALARM\r\n'''
    }
}
