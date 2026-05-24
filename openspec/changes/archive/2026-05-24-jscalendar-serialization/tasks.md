## 1. Foundation: shared mapping infrastructure

- [x] 1.1 Add a `JSCalendarPropertyMapper` (package-private, in `jmap`) with static helpers for the common scalar mappings: `applyUid`, `applyTitle`, `applyDescription`, `applyTimestamps` (`CREATED`→`created`, `LAST-MODIFIED`→`updated`), `applySequence`, `applyPriority`, `applyStatus` (with the iCalendar→JSCalendar value remapping per spec table), `applyCategoriesAsKeywords`, `applyColor`, `applyPrivacy` (`CLASS`→`privacy`).
- [x] 1.2 Add `JSCalendarTemporal` helper for the temporal split: given an ical4j `DtStart`/`Due`, return a small record-like object exposing `localDateTime` (ISO string), `timeZone` (`String?`), and `showWithoutTime` (`boolean`). Cover `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`.
- [x] 1.3 Add `JSCalendarDuration` helper that converts `Duration` and `DtStart`+`DtEnd` pairs into an ISO-8601 duration string (`PT1H30M`). Reuse `java.time.Duration.toString()` where possible.
- [x] 1.4 Add unit tests (Spock) for the three helpers above: `JSCalendarPropertyMapperTest`, `JSCalendarTemporalTest`, `JSCalendarDurationTest`.

## 2. Event and task builders

- [x] 2.1 Replace `JSEventSerializer.JSEventBuilder.build()` to emit a full `jsevent` per spec: call the shared scalar mappers, then `applyStartAndDuration`, then sub-object builders (participants, locations, alerts, recurrence). Keep the method ≤ 60 lines; extract anything larger to helpers.
- [x] 2.2 Implement `JSTaskSerializer.JSTaskBuilder.build()` — same shape as event, plus `due` (from `DUE`), `percentComplete` (from `PERCENT-COMPLETE`).
- [x] 2.3 Implement journal handling: `JSJournalSerializer` + `JSEventSerializer.JSJournalBuilder` emit a `jsevent` with `freeBusyStatus:"free"`.
- [x] 2.4 Add per-builder Spock specs (`JSEventSerializerTest`, `JSTaskSerializerTest`) that walk the scenarios from `specs/jscalendar-serialization/spec.md` (minimal event, time-zoned event, floating event, all-day event, DURATION, DTEND, categories, timestamps, status remap, percent-complete, unknown X- property).

## 3. Sub-object builders: participants, locations, alerts

- [x] 3.1 Implement `ParticipantBuilder` in `jmap`: takes an `Organizer` or `Attendee`, returns a `JsonNode` with `@type:"Participant"`, `sendTo.imip`, and a `roles` object derived from `ROLE`/whether it came from `ORGANIZER`.
- [x] 3.2 Wire participants into `JSEventBuilder` / `JSTaskBuilder`: group the calendar's organizer + attendees into a `participants` map keyed by a stable identifier (mailto-stripped address).
- [x] 3.3 Flesh out the existing `LocationBuilder` stub so it produces `{"@type":"Location","name":"<LOCATION value>"}`, and have it merge `GEO` into `coordinates` when present.
- [x] 3.4 Flesh out `VirtualLocationBuilder` for `CONFERENCE` properties (`@type:"VirtualLocation"`, `uri:`, `name:` from the `LABEL` parameter, `features:` from `FEATURE`).
- [x] 3.5 Add `AlertBuilder` that converts a `VAlarm` into a JSCalendar `Alert` (action, trigger as `OffsetTrigger` for duration triggers and `AbsoluteTrigger` for date-time triggers). Wire `alerts` into the event/task builders.
- [x] 3.6 Spock specs covering each sub-object builder in isolation: `ParticipantBuilderTest`, `LocationBuilderTest`, `VirtualLocationBuilderTest`, `AlertBuilderTest`.

## 4. Recurrence

- [x] 4.1 Implement `RecurrenceRuleBuilder` that converts an `RRULE` into a structured `RecurrenceRule` object (`frequency` from `FREQ`, plus `interval`, `count`, `until`, `byMonth`, `byDay`, `byMonthDay`, `byYearDay`, `bySetPosition`).
- [x] 4.2 In the event/task builders, wrap the rule in a one-element `recurrenceRules` array.
- [x] 4.3 Implement `RDATE`/`EXDATE` → `recurrenceOverrides`: each `RDATE` adds an empty-object entry at the date key; each `EXDATE` adds `{"excluded":true}`.
- [x] 4.4 Spock specs: weekly recurrence with interval+count, daily recurrence with EXDATE, monthly by-day rule, RDATE override (`RecurrenceRuleBuilderTest`).

## 5. JSContact: cards and groups

- [x] 5.1 Implement `JSCardSerializer.JSCardBuilder.build()` — map `FN` to `name.full`, `N` to structured `name` components (given/surname), `EMAIL` to `emails` map, `TEL` to `phones` map, `ADR` to `addresses` map, `URL` to `onlineServices`, `BDAY` to `anniversaries`. Now accepts a vCard `Entity` rather than the multi-card `VCard` wrapper.
- [x] 5.2 Implement `JSCardGroupSerializer.JSCardGroupBuilder.build()` for the vCard group-Entity model — `FN` → `name.full`, `MEMBER` → `members` map (URI→true). Accepts a vCard `Entity`. Covered by `JSCardGroupSerializerTest`.
- [x] 5.3 Implement `JSGroupSerializer.JSGroupBuilder.build()` for the JSCalendar `Group` wrapper around a `Calendar` — emits `@type:"Group"`, `prodId`, `uid`, and `entries` populated by recursively serializing child `VEVENT`/`VTODO`/`VJOURNAL` components.
- [x] 5.4 Spock specs (`JSCardSerializerTest`, `JSCardGroupSerializerTest`, `JSGroupSerializerTest`).

## 6. Jackson module + public API

- [x] 6.1 Add `JSCalendarModule extends SimpleModule` in `jmap` that registers `JSEventSerializer`, `JSTaskSerializer`, `JSJournalSerializer`, `JSGroupSerializer`, `JSCardSerializer` via `addSerializer(...)`. `JSCardGroupSerializer` is left as an opt-in registration because both `JSCardSerializer` and `JSCardGroupSerializer` operate on the same `Entity` type — consumers needing the CardGroup output register it explicitly.
- [x] 6.2 Verify the module exports stay correct in `src/main/java/module-info.java` — no new packages need exporting (everything stays under `org.mnode.ical4j.serializer.jmap`, which is already exported).
- [x] 6.3 Add a top-of-package Javadoc on `JSCalendarModule` listing the supported subset and explicit known limitations (deferred items from this change).
- [x] 6.4 `JSCalendarModuleTest` registers `JSCalendarModule` on an `ObjectMapper` and walks each fixture under `src/test/resources/samples/jscalendar/`, asserting the produced JSON equals the parsed `.json` golden.

## 7. Fixtures and documentation

- [x] 7.1 Fixtures under `src/test/resources/samples/jscalendar/`: `event-utc.ics`/`.json`, `event-zoned.ics`/`.json`, `event-allday.ics`/`.json`, `event-recurring.ics`/`.json`, `task-due.ics`/`.json`, `card-basic.vcf`/`.json`.
- [x] 7.2 README updated under `#### JSCalendar JSON format:` to use `JSCalendarModule` and show a realistic populated example.

## 8. Quality gate

- [x] 8.1 `./gradlew test` — all new Spock specs pass, existing tests remain green.
- [x] 8.2 `./gradlew check` runs clean.
- [x] 8.3 Self-review: every scenario in `specs/jscalendar-serialization/spec.md` is covered by at least one test. Coverage matrix:
  - Jackson Module / Consumer registers the module → `JSCalendarModuleTest`
  - Jackson Module / independent of jCal+xCal → `JSCalendarModuleTest` (fresh mapper)
  - JSEvent / minimal event with UTC start → `JSEventSerializerTest` + `event-utc` fixture
  - JSEvent / explicit time zone → `JSEventSerializerTest` + `event-zoned` fixture
  - JSEvent / floating-time → `JSEventSerializerTest`
  - JSEvent / all-day → `JSEventSerializerTest` + `event-allday` fixture
  - JSEvent / DURATION → `JSEventSerializerTest`
  - JSEvent / DTEND → `JSEventSerializerTest`
  - JSTask / due date → `JSTaskSerializerTest` + `task-due` fixture
  - JSTask / percent-complete → `JSTaskSerializerTest`
  - Common Property Mapping / categories → `JSEventSerializerTest` + `JSCalendarPropertyMapperTest`
  - Common Property Mapping / created+lastmodified → `JSEventSerializerTest` + `JSCalendarPropertyMapperTest`
  - Common Property Mapping / status remap → `JSEventSerializerTest` + `JSCalendarPropertyMapperTest` (all values)
  - Participant / organizer + attendee → `JSEventSerializerTest` + `ParticipantBuilderTest`
  - Location / named location → `LocationBuilderTest`
  - Location / GEO coordinates → `LocationBuilderTest`
  - Recurrence / weekly → `RecurrenceRuleBuilderTest` + `event-recurring` fixture
  - Recurrence / EXDATE → `RecurrenceRuleBuilderTest`
  - Alarm / DISPLAY -PT15M → `AlertBuilderTest`
  - JSContact Card / FN + EMAIL → `JSCardSerializerTest` + `card-basic` fixture
  - JSContact CardGroup / members → `JSCardGroupSerializerTest`
  - JSCalendar Group / calendar w/ event+task → `JSGroupSerializerTest`
  - Unknown Property Tolerance / X- property → `JSEventSerializerTest`
