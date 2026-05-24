# JSCalendar Serialization

JSCalendar (RFC 8984) and JSContact (RFC 9610) serialization for ical4j components via Jackson serializers in the `org.mnode.ical4j.serializer.jmap` package.

## Requirements

### Requirement: JSCalendar Jackson Module
The library SHALL provide a Jackson `SimpleModule` named `JSCalendarModule` in the `org.mnode.ical4j.serializer.jmap` package that registers JSCalendar serializers for `VEvent`, `VToDo`, `VJournal`, `VCard`, and `Group`.

#### Scenario: Consumer registers the module
- **WHEN** a consumer calls `objectMapper.registerModule(new JSCalendarModule())`
- **THEN** subsequent calls to `objectMapper.writeValueAsString(vEvent)` SHALL produce RFC 8984 JSCalendar JSON
- **AND** the same applies to `VToDo`, `VJournal`, `VCard`, and `Group` instances

#### Scenario: Module is independent of jCal/xCal registrations
- **WHEN** a consumer registers only `JSCalendarModule` on a fresh `ObjectMapper`
- **THEN** JSCalendar serialization SHALL work without requiring any other ical4j-serializer modules to be registered

### Requirement: JSEvent Serialization
A `VEvent` SHALL serialize to a JSON object whose `@type` field equals `"jsevent"` and whose property set conforms to RFC 8984 §5.1.

#### Scenario: Minimal event with UID, summary, and start
- **GIVEN** a `VEvent` with `UID:abc@example.com`, `SUMMARY:Meeting`, `DTSTART:20240516T090000Z`
- **WHEN** the event is serialized via `JSEventSerializer`
- **THEN** the output JSON SHALL contain `"@type":"jsevent"`, `"uid":"abc@example.com"`, `"title":"Meeting"`, `"start":"2024-05-16T09:00:00"`, `"timeZone":"Etc/UTC"`

#### Scenario: Event with explicit time zone
- **GIVEN** a `VEvent` with `DTSTART;TZID=Australia/Sydney:20240516T090000`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"start":"2024-05-16T09:00:00"` and `"timeZone":"Australia/Sydney"`

#### Scenario: Floating-time event
- **GIVEN** a `VEvent` with `DTSTART:20240516T090000` (no TZID, not UTC)
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"start":"2024-05-16T09:00:00"` and SHALL NOT contain a `timeZone` field

#### Scenario: All-day event
- **GIVEN** a `VEvent` with `DTSTART;VALUE=DATE:20240516`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"start":"2024-05-16T00:00:00"` and `"showWithoutTime":true`

#### Scenario: Event with duration
- **GIVEN** a `VEvent` with `DURATION:PT1H30M`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"duration":"PT1H30M"`

#### Scenario: Event with DTEND (converted to duration)
- **GIVEN** a `VEvent` with `DTSTART:20240516T090000Z` and `DTEND:20240516T103000Z`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"duration":"PT1H30M"` and SHALL NOT contain a `dtend` or `end` field

### Requirement: JSTask Serialization
A `VToDo` SHALL serialize to a JSON object whose `@type` field equals `"jstask"` and whose property set conforms to RFC 8984 §5.2.

#### Scenario: Task with due date
- **GIVEN** a `VToDo` with `UID:t1`, `SUMMARY:Pay bills`, `DUE:20240520T170000Z`
- **WHEN** the task is serialized via `JSTaskSerializer`
- **THEN** the output SHALL contain `"@type":"jstask"`, `"uid":"t1"`, `"title":"Pay bills"`, `"due":"2024-05-20T17:00:00"`, `"timeZone":"Etc/UTC"`

#### Scenario: Task with percent-complete
- **GIVEN** a `VToDo` with `PERCENT-COMPLETE:50`
- **WHEN** the task is serialized
- **THEN** the output SHALL contain `"percentComplete":50`

### Requirement: Common Property Mapping
For the JSCalendar object types `jsevent` and `jstask`, the following iCalendar properties SHALL map to the named JSCalendar fields when present.

| iCalendar | JSCalendar | Notes |
|-----------|------------|-------|
| `UID` | `uid` | string |
| `SUMMARY` | `title` | string |
| `DESCRIPTION` | `description` | string |
| `CREATED` | `created` | UTC date-time string |
| `LAST-MODIFIED` | `updated` | UTC date-time string |
| `SEQUENCE` | `sequence` | integer |
| `PRIORITY` | `priority` | integer |
| `STATUS` | `status` | remapped (see below) |
| `CATEGORIES` | `keywords` | object whose keys are the category strings, values `true` |
| `COLOR` | `color` | string |
| `URL` | one `links` entry with `rel:"about"` | structured |
| `CLASS` | `privacy` | `"public"`/`"private"`/`"secret"` |

`STATUS` values SHALL be remapped: iCalendar `CONFIRMED`→`"confirmed"`, `TENTATIVE`→`"tentative"`, `CANCELLED`→`"cancelled"`; for `VToDo`, `IN-PROCESS`→`"in-process"`, `COMPLETED`→`"completed"`, `NEEDS-ACTION`→`"needs-action"`.

#### Scenario: Categories map to keywords object
- **GIVEN** a `VEvent` with `CATEGORIES:meeting,work`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"keywords":{"meeting":true,"work":true}`

#### Scenario: Created and last-modified timestamps
- **GIVEN** a `VEvent` with `CREATED:20240101T120000Z` and `LAST-MODIFIED:20240210T080000Z`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"created":"2024-01-01T12:00:00Z"` and `"updated":"2024-02-10T08:00:00Z"`

#### Scenario: Status remapping
- **GIVEN** a `VEvent` with `STATUS:CONFIRMED`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"status":"confirmed"`

### Requirement: Participant Serialization
`ORGANIZER` and `ATTENDEE` properties SHALL serialize to entries in the JSCalendar `participants` map.

Each entry SHALL be keyed by a stable identifier derived from the calendar address (e.g. the address itself, percent-decoded), and the value SHALL be a JSON object containing at minimum `"@type":"Participant"`, `"sendTo":{"imip":"<mailto-uri>"}`, and `"roles"` derived from the participant's `ROLE` parameter (`"attendee":true`, plus `"chair":true` for chairs and `"owner":true` for organizers).

#### Scenario: Event with one organizer and one attendee
- **GIVEN** a `VEvent` with `ORGANIZER:mailto:a@example.com` and `ATTENDEE:mailto:b@example.com`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain a `"participants"` object with two entries
- **AND** the entry for `a@example.com` SHALL contain `"roles":{"owner":true,"attendee":true}`
- **AND** the entry for `b@example.com` SHALL contain `"roles":{"attendee":true}`

### Requirement: Location Serialization
`LOCATION` SHALL produce a `locations` entry of `@type:"Location"`. `GEO` SHALL populate the `coordinates` field on the same or a sibling location.

#### Scenario: Event with named location
- **GIVEN** a `VEvent` with `LOCATION:Conference Room A`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"locations":{"<id>":{"@type":"Location","name":"Conference Room A"}}`

#### Scenario: Event with GEO coordinates
- **GIVEN** a `VEvent` with `GEO:-33.8688;151.2093`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain a `locations` entry whose `coordinates` field equals `"geo:-33.8688,151.2093"`

### Requirement: Recurrence Rule Serialization
A single `RRULE` SHALL serialize to a one-element `recurrenceRules` array containing a structured `RecurrenceRule` object. `RDATE` and `EXDATE` SHALL serialize to `recurrenceOverrides` entries; `EXDATE` entries SHALL include `"excluded":true`.

#### Scenario: Weekly recurrence
- **GIVEN** a `VEvent` with `RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=10`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"recurrenceRules":[{"@type":"RecurrenceRule","frequency":"weekly","interval":2,"count":10}]`

#### Scenario: Recurrence with exception dates
- **GIVEN** a `VEvent` with `RRULE:FREQ=DAILY` and `EXDATE:20240520T090000Z`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain `"recurrenceOverrides":{"2024-05-20T09:00:00":{"excluded":true}}`

### Requirement: Alarm Serialization
Each `VALARM` SHALL serialize to an entry in the `alerts` map, keyed by an alarm-local identifier, with `@type:"Alert"`. The `TRIGGER` property SHALL map to a structured `trigger` object (`OffsetTrigger` for duration triggers, `AbsoluteTrigger` for date-time triggers).

#### Scenario: Display alarm 15 minutes before start
- **GIVEN** a `VEvent` containing a `VALARM` with `ACTION:DISPLAY` and `TRIGGER:-PT15M`
- **WHEN** the event is serialized
- **THEN** the output SHALL contain an `"alerts"` entry whose value matches `{"@type":"Alert","action":"display","trigger":{"@type":"OffsetTrigger","offset":"-PT15M"}}`

### Requirement: JSContact Card Serialization
A `VCard` SHALL serialize to a JSON object whose `@type` field equals `"Card"` and whose property set conforms to RFC 9610.

#### Scenario: Card with full name and email
- **GIVEN** a `VCard` with `FN:Jane Doe` and `EMAIL:jane@example.com`
- **WHEN** the card is serialized via `JSCardSerializer`
- **THEN** the output SHALL contain `"@type":"Card"`, `"name":{"full":"Jane Doe"}`, and an `"emails"` object containing one entry with `"address":"jane@example.com"`

### Requirement: JSContact CardGroup Serialization
A vCard `Entity` with `KIND:group` SHALL serialize to a JSON object whose `@type` field equals `"CardGroup"`, with `members` populated from the entity's `MEMBER` properties.

#### Scenario: Group with members
- **GIVEN** an `Entity` with `KIND:group`, `FN:Marketing`, and two `MEMBER:urn:uuid:...` entries
- **WHEN** the group is serialized via `JSCardGroupSerializer`
- **THEN** the output SHALL contain `"@type":"CardGroup"`, `"name":{"full":"Marketing"}`, and `"members"` containing both UUIDs as keys with value `true`

### Requirement: JSCalendar Group Serialization
A `Calendar` SHALL serialize to a JSON object whose `@type` field equals `"Group"` (RFC 8984 §5.3) with an `entries` array containing the JSCalendar representation of each child `VEVENT`/`VTODO`/`VJOURNAL`.

#### Scenario: Calendar containing event and task
- **GIVEN** a `Calendar` with `PRODID`, `UID:cal-1`, a `VEVENT` (`UID:evt-1`), and a `VTODO` (`UID:tsk-1`)
- **WHEN** the calendar is serialized via `JSGroupSerializer`
- **THEN** the output SHALL contain `"@type":"Group"`, `"prodId":"..."`, `"uid":"cal-1"`, and an `"entries"` array of two objects whose `@type` values are `"jsevent"` and `"jstask"` respectively

### Requirement: Unknown Property Tolerance
When the serializer encounters an iCalendar property that has no defined JSCalendar mapping, it SHALL silently omit that property from the output rather than failing.

#### Scenario: Event with X- extension property
- **GIVEN** a `VEvent` with a non-standard `X-CUSTOM:foo` property
- **WHEN** the event is serialized
- **THEN** the serializer SHALL complete without error
- **AND** the output JSON SHALL NOT contain any field derived from `X-CUSTOM`
