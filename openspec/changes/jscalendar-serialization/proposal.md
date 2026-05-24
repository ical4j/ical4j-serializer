## Why

The `org.mnode.ical4j.serializer.jmap` package contains stub serializers (`JSEventSerializer`, `JSTaskSerializer`, `JSGroupSerializer`, `JSCardSerializer`, `JSCardGroupSerializer`) whose `build()` methods emit only `{"@type": "jsevent"}` and nothing else. The project already ships jCal (RFC 7265), xCal (RFC 6321), and several JSON-LD/RDF representations, but lacks a working JSCalendar (RFC 8984) serializer — the JMAP-aligned JSON format consumers increasingly expect for calendar data.

## What Changes

- Complete the `jmap` package so that `VEvent`, `VToDo`, `VJournal`, and `VCard`/`Group` components serialize to RFC 8984 / RFC 9610 conformant JSON.
- Map the core JSCalendar properties for events and tasks: `uid`, `created`, `updated`, `start`, `duration`, `timeZone`, `title`, `description`, `descriptionContentType`, `keywords`, `categories`, `color`, `priority`, `privacy`, `status`, `freeBusyStatus`, `sequence`, `method`, `prodId`, `relatedTo`.
- Map structured sub-objects: participants (from `ATTENDEE`/`ORGANIZER`), locations (from `LOCATION`/`GEO`), virtual locations (from `CONFERENCE`), links (from `URL`/`ATTACH`), alerts (from `VALARM`), and recurrence rules/overrides (from `RRULE`/`RDATE`/`EXDATE`/`RECURRENCE-ID`).
- Map JSContact properties for cards and groups: `name`, `nicknames`, `organizations`, `titles`, `emails`, `phones`, `onlineServices`, `addresses`, `anniversaries`, `members`.
- Format temporal values as RFC 8984 `LocalDateTime` / `Duration` strings (e.g. `"2024-05-16T09:00:00"`, `"PT1H"`) — not the jCal array shapes used elsewhere.
- Wire all completed serializers into a Jackson `SimpleModule` (`JSCalendarModule`) so downstream consumers can register the format with one call.
- **BREAKING**: existing callers that rely on the stub output `{"@type":"jsevent"}` (and equivalents for task/group/card) will receive a populated object instead. There are no known production consumers — these are emitted as empty stubs today — but this is a behavioural change at the public-API level.

## Capabilities

### New Capabilities
- `jscalendar-serialization`: Convert ical4j `Calendar`, `VEvent`, `VToDo`, `VJournal`, `VCard`, and `Group` instances to RFC 8984 / RFC 9610 JSON via Jackson serializers in the `org.mnode.ical4j.serializer.jmap` package.

### Modified Capabilities
<!-- None — `openspec/specs/` is empty; no existing capability specs to amend. -->

## Impact

- **Code**: `src/main/java/org/mnode/ical4j/serializer/jmap/**` — replace stub `build()` bodies, add `JSCalendarModule`, add property-to-JSCalendar mapping helpers. The shared `JsonBuilder` interface in the root package may gain JSCalendar-flavoured type helpers (parallel to `encodeValue` for jCal).
- **Module**: `src/main/java/module-info.java` already exports `org.mnode.ical4j.serializer.jmap`; no module-graph changes expected.
- **Dependencies**: no new runtime dependencies — Jackson 2.17 and ical4j 4.2 are sufficient. ical4j-vcard 2.1 covers the `VCard`/`Group` model.
- **APIs**: the public `JSEventSerializer.JSEventBuilder` (and siblings) gain a real implementation; their constructors and class signatures stay stable. The new `JSCalendarModule` is additive.
- **Tests**: new Spock specs under `src/test/groovy/org/mnode/ical4j/serializer/jmap/`, with golden-file fixtures in `src/test/resources/samples/jscalendar/` (`.ics` source paired with `.json` expected output), following the existing jCal fixture convention.
- **Out of scope**: deserialization (JSCalendar → ical4j), the separate `jotn` "jot notation" format, and ActivityStreams/RDF builders already on `develop`.
