# iCal4j Javascript Object Notation

The purpose of this library is to provide custom marshalling between iCal4j objects and JSON formats.

## Overview

The following is a non-exhaustive list of known JSON calendar formats:

* [jCal](https://tools.ietf.org/html/rfc7265) - The JSON Format for iCalendar
* [JSCalendar](https://tools.ietf.org/html/draft-ietf-calext-jscalendar-32) - A JSON representation of calendar data (currently a draft specification)

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
Calendar calendar = ...;

SimpleModule module = new SimpleModule();
module.addSerializer(Calendar.class, new JSCalendarSerializer());
ObjectMapper mapper = new ObjectMapper();
mapper.registerModule(module);

String serialized = mapper.writeValueAsString(calendar);
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
