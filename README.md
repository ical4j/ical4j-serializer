# iCal4j Serializer

[jCal]: https://tools.ietf.org/html/rfc7265
[xCal]: https://www.rfc-editor.org/rfc/rfc6321.html
[JSCalendar]: https://datatracker.ietf.org/doc/html/rfc8984
[xCard]: https://www.rfc-editor.org/rfc/rfc6351.html
[jCard]: https://tools.ietf.org/html/rfc7095
[JSCard]: https://datatracker.ietf.org/doc/html/draft-ietf-jmap-jscontact
[Schema.org]: https://schema.org/
[JSON-LD]: https://json-ld.org/
[Jot API]: https://github.com/micronode/jotapi
[CalDAV]: https://tools.ietf.org/html/rfc4791
[JSON Feed]: https://www.jsonfeed.org/version/1.1/

The purpose of this library is to provide custom marshalling between iCal4j objects and other formats.

## Overview

Interoperability is a very important aspect of the iCalendar specification(s), and as such it is important
to support seamless and accurate translation between different data formats. There are many alternate formats
using JSON and XML, and as such this library aims to provide a mechanism for conversion
between iCalendar objects and other popular JSON and XML-based formats.

The following calendar formats are the current focus of this library.

### jCal - The JSON Format for iCalendar

[jCal] is the official (standard) JSON respresentation of iCalendar
object data. This format is designed to be a "lossless" format in that no semantic or syntactic data is lost
when translating between iCalendar objects and this JSON format.

This format is a good choice when unambiguous interoperability is required, as in theory if an agent supports
the iCalendar specification then it should be reasonably trivial to have equivalent support for this format.

### xCal - The XML Format for iCalendar

TBD.

### JSCalendar - A JSON representation of calendar data 

[JSCalendar] is a relatively new format
(currently a draft specification), and aims to simplify the data representation of iCalendar data by focusing
on the key aspects that are primarily supported in most calendar agents. The focus of JSCalendar is on events
and tasks, and does not include support for less widely used aspects of iCalendar such as journaling and
availability.

This format is good for calendar agents that do not yet have support for iCalendar, but would like to support
interoperability with other calendar agents. As this is still a draft standard it is subject to change.

### jCard - The JSON format for vCard

As iCal4j includes a vCard object representation, it also makes sense to support JSON conversion of this
object model. [jCard] provides a specification for converting JSON to/from vCard data.

### JSCard - A JSON representation for contact data

[JSCard] provides a specification for converting JSON to/from vCard data.


### Schema.org - shared vocabulary for structured Web content

[Schema.org] presents a collection of collaborative data models to represent data on the Web. This includes
data representations in [JSON-LD] format, which is included here.

### JSON Feed - a format similar to RSS and Atom but in JSON

[JSON Feed] is a pragmatic syndication format, like RSS and Atom, but with one big difference:
it’s JSON instead of XML.


### Jot API - An open REST API based on the iCalendar specification

The [Jot API] is not a standard format, and does not have the same goals
of interoperability that jCal and JSCalendar provide. Jot is designed to be an API for managing and persisting
iCalendar data, and includes object formats and validation consistent with the iCalendar specification.

The Jot model format can be more directly compared with calendar synchronization standards such as [CalDAV],
however at this stage does not provide many of the features of such standards. This library may be of 
benefit if you are building a calendar agent that requires simple calendar persistence (and support for CalDAV is not
available), or if creating a persistence layer that supports the Jot API. 

## Implementation

This project uses the well-established Jackson library for JSON parsing and object model. The full list of
dependencies includes:

* ical4j
* jackson-databind
* slf4j

## Usage

### Serialization

#### jCal JSON format:

```java
Calendar calendar = ...;

SimpleModule module = new SimpleModule();
module.addSerializer(Calendar.class, new JCalSerializer());
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

String serialized = mapper.writeValueAsString(calendar);
```

Result:

```
["vcalendar",[["prodid",{},"text","-//Ben Fortuna//iCal4j 3.1//EN"],["version",{},"text","2.0"],["uid",{},"text","123"]],[["vevent",[["uid",{},"text","1"],["summary",{},"text","Test Event 1"],["dtstart",{"value":"date"},"date","20090810"],["action",{},"text","DISPLAY"],["attach",{"encoding":"base64","value":"binary"},"binary","..."]],[]],["vevent",[["uid",{},"text","2"],["summary",{},"text","Test Event 2"],["dtstart",{"value":"date"},"date","20100810"]],[]]]]
```


#### JSCalendar JSON format:

```java
VEvent event = ...;

SimpleModule module = new SimpleModule();
module.addSerializer(VEvent.class, new JSEventSerializer());
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

String serialized = mapper.writeValueAsString(event);
```

Result:

```
{"@type":"jsevent"}
```

#### Jot API serialization

```java
VJournal journal  = ...;

SimpleModule module = new SimpleModule();
module.addSerializer(VJournal.class, new JotJournalSerializer());
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

String serialized = mapper.writeValueAsString(journal);
```

### Deserialization

```java
String json = ...;

SimpleModule module = new SimpleModule();
module.addDeserializer(Calendar.class, new JCalMapper())
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

Calendar calendar = mapper.readValue(json, Calendar.class);
```

## Command Line Usage

### Serialize a calendar to default output (jCal)

    ical4j-serializer/bin/ical4j-serializer calendar -F ./Australian32Holidays.ics

Result:

```json
["vcalendar",[["calscale",{},"text","GREGORIAN"],["method",{},"text","PUBLISH"],["prodid",{},"text","-//Apple Computer, Inc//iCal 1.0//EN"],["x-wr-calname",{"value":"text"},"text","Australian Holidays"],["x-wr-relcalid",{"value":"text"},"text","D4167B74-C414-11D6-BA97-003065F198AC"],["x-wr-timezone",{"value":"text"},"text","Asia/Hong_Kong"],["version",{},"text","2.0"]],[["vtimezone",[["tzid",{},"text","Asia/Hong_Kong"],["last-modified",{},"date-time","2006-01-17T16:36:57Z"]],[["standard",[["dtstart",{},"date-time","1932-12-13T20:45:52"],["tzoffsetto",{},"utc-offset","+08:00"],["tzoffsetfrom",{},"utc-offset","Z"],["tzname",{},"text","HKT"]],[]],["daylight",[["dtstart",{},"date-time","1946-04-20T03:30:00"],["tzoffsetto",{},"utc-offset","+09:00"],["tzoffsetfrom",{},"utc-offset","+08:00"],["tzname",{},"text","HKST"]],[]],["standard",[["dtstart",{},"date-time","1946-12-01T03:30:00"],["tzoffsetto",{},"utc-offset","+08:00"],["tzoffsetfrom",{},"utc-offset","+09:00"],["tzname",{},"text","HKT"]],[]]]],["vevent",[["uid",{},"text","D416469E-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Australia Day"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=1"],["dtstart",{"value":"date"},"date","2002-01-26"],["dtend",{"value":"date"},"date","2002-01-27"]],[]],["vevent",[["uid",{},"text","D4164CA4-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Good Friday"],["dtstart",{"value":"date"},"date","2002-03-29"],["dtend",{"value":"date"},"date","2002-03-30"]],[]],["vevent",[["uid",{},"text","D416509A-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Easter Monday"],["dtstart",{"value":"date"},"date","2002-04-01"],["dtend",{"value":"date"},"date","2002-04-02"]],[]],["vevent",[["uid",{},"text","D41654CC-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","ANZAC Day"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=4"],["dtstart",{"value":"date"},"date","2002-04-25"],["dtend",{"value":"date"},"date","2002-04-26"]],[]],["vevent",[["uid",{},"text","D41658EB-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Queen's Birthday"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=6"],["dtstart",{"value":"date"},"date","2002-06-10"],["dtend",{"value":"date"},"date","2002-06-11"]],[]],["vevent",[["uid",{},"text","D41661F7-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Christmas"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=12"],["dtstart",{"value":"date"},"date","2002-12-25"],["dtend",{"value":"date"},"date","2002-12-26"]],[]],["vevent",[["uid",{},"text","D41666AF-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Boxing Day"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=12"],["dtstart",{"value":"date"},"date","2002-12-26"],["dtend",{"value":"date"},"date","2002-12-27"]],[]],["vevent",[["uid",{},"text","D4166AA4-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Good Friday"],["dtstart",{"value":"date"},"date","2003-04-18"],["dtend",{"value":"date"},"date","2003-04-19"]],[]],["vevent",[["uid",{},"text","D4166EA4-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:44:59Z"],["summary",{},"text","Easter Monday"],["dtstart",{"value":"date"},"date","2003-04-21"],["dtend",{"value":"date"},"date","2003-04-22"]],[]],["vevent",[["uid",{},"text","D41658EB-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:45:00Z"],["recurrence-id",{"tzid":"asia/hong_kong"},"date-time","2003-06-10T00:00:00"],["dtstart",{"value":"date"},"date","2003-06-09"],["dtend",{"value":"date"},"date","2003-06-10"]],[]],["vevent",[["uid",{},"text","D41672C8-C414-11D6-BA97-003065F198AC"],["dtstamp",{},"date-time","2002-09-06T09:45:00Z"],["summary",{},"text","New Year"],["rrule",{},"recur","FREQ=YEARLY;INTERVAL=1;BYMONTH=1"],["dtstart",{"value":"date"},"date","2002-01-01"],["dtend",{"value":"date"},"date","2002-01-02"]],[]]]]
```

### Serialize a calendar to XML (xCal)

    ical4j-serializer/bin/ical4j-serializer calendar -F ./Australian32Holidays.ics --pretty-print -X XCAL

Result:

```xml
<icalendar xmlns="urn:ietf:params:xml:ns:icalendar-2.0">
  <vcalendar>
    <properties>
      <calscale>
        <parameters/>
        <text>GREGORIAN</text>
      </calscale>
      <method>
        <parameters/>
        <text>PUBLISH</text>
      </method>
      <prodid>
        <parameters/>
        <text>-//Apple Computer, Inc//iCal 1.0//EN</text>
      </prodid>
      <x-wr-calname>
        <parameters>
          <value>text</value>
        </parameters>
        <text>Australian Holidays</text>
      </x-wr-calname>
      <x-wr-relcalid>
        <parameters>
          <value>text</value>
        </parameters>
        <text>D4167B74-C414-11D6-BA97-003065F198AC</text>
      </x-wr-relcalid>
      <x-wr-timezone>
        <parameters>
          <value>text</value>
        </parameters>
        <text>Asia/Hong_Kong</text>
      </x-wr-timezone>
      <version>
        <parameters/>
        <text>2.0</text>
      </version>
    </properties>
    <components>
      <vtimezone>
        <properties>
          <tzid>
            <parameters/>
            <text>Asia/Hong_Kong</text>
          </tzid>
          <last-modified>
            <parameters/>
            <date-time>2006-01-17T16:36:57Z</date-time>
          </last-modified>
        </properties>
        <components>
          <standard>
            <properties>
              <dtstart>
                <parameters/>
                <date-time>1932-12-13T20:45:52</date-time>
              </dtstart>
              <tzoffsetto>
                <parameters/>
                <utc-offset>+08:00</utc-offset>
              </tzoffsetto>
              <tzoffsetfrom>
                <parameters/>
                <utc-offset>Z</utc-offset>
              </tzoffsetfrom>
              <tzname>
                <parameters/>
                <text>HKT</text>
              </tzname>
            </properties>
            <components/>
          </standard>
          <daylight>
            <properties>
              <dtstart>
                <parameters/>
                <date-time>1946-04-20T03:30:00</date-time>
              </dtstart>
              <tzoffsetto>
                <parameters/>
                <utc-offset>+09:00</utc-offset>
              </tzoffsetto>
              <tzoffsetfrom>
                <parameters/>
                <utc-offset>+08:00</utc-offset>
              </tzoffsetfrom>
              <tzname>
                <parameters/>
                <text>HKST</text>
              </tzname>
            </properties>
            <components/>
          </daylight>
        </components>
      </vtimezone>
      <vevent>
        <properties>
          <uid>
            <parameters/>
            <text>D416469E-C414-11D6-BA97-003065F198AC</text>
          </uid>
          <dtstamp>
            <parameters/>
            <date-time>2002-09-06T09:44:59Z</date-time>
          </dtstamp>
          <summary>
            <parameters/>
            <text>Australia Day</text>
          </summary>
          <rrule>
            <parameters/>
            <recur>FREQ=YEARLY;INTERVAL=1;BYMONTH=1</recur>
          </rrule>
          <dtstart>
            <parameters>
              <value>date</value>
            </parameters>
            <date>2002-01-26</date>
          </dtstart>
          <dtend>
            <parameters>
              <value>date</value>
            </parameters>
            <date>2002-01-27</date>
          </dtend>
        </properties>
        <components/>
      </vevent>
    </components>
  </vcalendar>
</icalendar>
```

## References

* [RFC5545](https://tools.ietf.org/html/rfc5545) (iCalendar)
* [RFC7953](https://datatracker.ietf.org/doc/html/rfc7953) (iCalendar Availability)
* [RFC6350](https://datatracker.ietf.org/doc/html/rfc6350) (vCard)
* [RFC7265](https://tools.ietf.org/html/rfc7265) (jCal)
* [RFC6321](https://www.rfc-editor.org/rfc/rfc6321.html) (xCal)
* [RFC7095](https://tools.ietf.org/html/rfc7095) (jCard)
* [RFC6351](https://www.rfc-editor.org/rfc/rfc6351.html) (xCard)
* [RFC8984](https://datatracker.ietf.org/doc/html/rfc8984) (JSCalendar)
* [JSCalendar to iCalendar Draft](https://datatracker.ietf.org/doc/html/draft-ietf-calext-jscalendar-icalendar-05)
* [JSContact Draft](https://datatracker.ietf.org/doc/html/draft-ietf-jmap-jscontact)
* [Schema.org - Event](https://schema.org/Event)
* [Jot API Models](https://github.com/micronode/jotapi/tree/main/models)
