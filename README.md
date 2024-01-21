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

The purpose of this library is to provide custom marshalling between iCal4j objects and JSON/XML-based formats.

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

### jCard - The JSON format for vCard

As iCal4j includes a vCard object representation, it also makes sense to support JSON conversion of this
object model. [jCard] provides a specification for converting JSON to/from vCard data.

### xCal - The XML Format for iCalendar

TBD.

### JMAP - A JSON representation of calendar/contact data 

[JSCalendar] is a relatively new format
(currently a draft specification), and aims to simplify the data representation of iCalendar data by focusing
on the key aspects that are primarily supported in most calendar agents. The focus of JSCalendar is on events
and tasks, and does not include support for less widely used aspects of iCalendar such as journaling and
availability.

This format is good for calendar agents that do not yet have support for iCalendar, but would like to support
interoperability with other calendar agents. As this is still a draft standard it is subject to change.

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
