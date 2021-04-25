# iCal4j Javascript Object Notation

[jCal]: https://tools.ietf.org/html/rfc7265
[JSCalendar]: https://tools.ietf.org/html/draft-ietf-calext-jscalendar-32
[Jot API]: https://github.com/micronode/jotapi
[CalDAV]: https://tools.ietf.org/html/rfc4791

The purpose of this library is to provide custom marshalling between iCal4j objects and JSON formats.

## Overview

Interoperability is a very important aspect of the iCalendar specification(s), and as such it is important
to support seamless and accurate translation between different data formats. JSON is currently the de-facto
standard for structural data formats, and as such this library aims to provide a mechanism for conversion
between iCalendar objects and JSON formats.

The following JSON calendar formats are the current focus of this library.

### jCal - The JSON Format for iCalendar

[jCal] is the official (standard) JSON respresentation of iCalendar
object data. This format is designed to be a "lossless" format in that no semantic or syntactic data is lost
when translating between iCalendar objects and this JSON format.

This format is a good choice when unambiguous interoperability is required, as in theory if an agent supports
the iCalendar specification then it should be reasonably trivial to have equivalent support for this format.

### JSCalendar - A JSON representation of calendar data 

[JSCalendar] is a relatively new format
(currently a draft specification), and aims to simplify the data representation of iCalendar data by focusing
on the key aspects that are primarily supported in most calendar agents. The focus of JSCalendar is on events
and tasks, and does not include support for less widely used aspects of iCalendar such as journaling and
availability.

This format is good for calendar agents that do not yet have support for iCalendar, but would like to support
interoperability with other calendar agents. As this is still a draft standard it is subject to change.

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

#### JSCalendar JSON format:

```java
VEvent event = ...;

SimpleModule module = new SimpleModule();
module.addSerializer(VEvent.class, new JSEventSerializer());
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

String serialized = mapper.writeValueAsString(event);
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
* [RFC7265](https://tools.ietf.org/html/rfc7265) (jCal)
* [JSCalendar Draft](https://tools.ietf.org/html/draft-ietf-calext-jscalendar-32)
* [JSCalendar to iCalendar Draft](https://datatracker.ietf.org/doc/html/draft-ietf-calext-jscalendar-icalendar-04)
* [Jot API Models](https://github.com/micronode/jotapi/tree/main/models)
