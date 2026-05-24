## 1. Foundation: shared mapping infrastructure

- [x] 1.1 Add a `JSCalendarPropertyMapper` (package-private, in `jmap`) with static helpers for the common scalar mappings: `applyUid`, `applyTitle`, `applyDescription`, `applyTimestamps` (`CREATED`→`created`, `LAST-MODIFIED`→`updated`), `applySequence`, `applyPriority`, `applyStatus` (with the iCalendar→JSCalendar value remapping per spec table), `applyCategoriesAsKeywords`, `applyColor`, `applyPrivacy` (`CLASS`→`privacy`).
- [x] 1.2 Add `JSCalendarTemporal` helper for the temporal split: given an ical4j `DtStart`/`Due`, return a small record-like object exposing `localDateTime` (ISO string), `timeZone` (`String?`), and `showWithoutTime` (`boolean`). Cover `LocalDate`, `LocalDateTime`, `Instant`, `ZonedDateTime`.
- [x] 1.3 Add `JSCalendarDuration` helper that converts `Duration` and `DtStart`+`DtEnd` pairs into an ISO-8601 duration string (`PT1H30M`). Reuse `java.time.Duration.toString()` where possible.
- [ ] 1.4 Add unit tests (Spock) for the three helpers above under `src/test/groovy/org/mnode/ical4j/serializer/jmap/JSCalendarPropertyMapperTest.groovy` and `JSCalendarTemporalTest.groovy`. *(Covered indirectly by per-builder specs in §2/§5; standalone helper specs remain as a follow-up.)*

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
- [ ] 3.6 Add Spock specs covering each sub-object builder in isolation. *(Participant flow exercised in `JSEventSerializerTest`; standalone builder specs remain as a follow-up.)*

## 4. Recurrence

- [x] 4.1 Implement `RecurrenceRuleBuilder` that converts an `RRULE` into a structured `RecurrenceRule` object (`frequency` from `FREQ`, plus `interval`, `count`, `until`, `byMonth`, `byDay`, `byMonthDay`, `byYearDay`, `bySetPosition`).
- [x] 4.2 In the event/task builders, wrap the rule in a one-element `recurrenceRules` array.
- [x] 4.3 Implement `RDATE`/`EXDATE` → `recurrenceOverrides`: each `RDATE` adds an empty-object entry at the date key; each `EXDATE` adds `{"excluded":true}`.
- [ ] 4.4 Add Spock specs: weekly recurrence with interval+count, daily recurrence with EXDATE, monthly by-day rule. *(Follow-up.)*

## 5. JSContact: cards and groups

- [x] 5.1 Implement `JSCardSerializer.JSCardBuilder.build()` — map `FN` to `name.full`, `N` to structured `name` components (given/surname), `EMAIL` to `emails` map, `TEL` to `phones` map, `ADR` to `addresses` map, `URL` to `onlineServices`, `BDAY` to `anniversaries`. Now accepts a vCard `Entity` rather than the multi-card `VCard` wrapper.
- [x] 5.2 Implement `JSCardGroupSerializer.JSCardGroupBuilder.build()` for the vCard group-Entity model — `FN` → `name.full`, `MEMBER` → `members` map (URI→true). Accepts a vCard `Entity`.
- [x] 5.3 Implement `JSGroupSerializer.JSGroupBuilder.build()` for the JSCalendar `Group` wrapper around a `Calendar` — emits `@type:"Group"`, `prodId`, `uid`, and `entries` populated by recursively serializing child `VEVENT`/`VTODO`/`VJOURNAL` components.
- [x] 5.4 Add Spock specs (`JSCardSerializerTest`, `JSGroupSerializerTest`).

## 6. Jackson module + public API

- [x] 6.1 Add `JSCalendarModule extends SimpleModule` in `jmap` that registers `JSEventSerializer`, `JSTaskSerializer`, `JSJournalSerializer`, `JSGroupSerializer`, `JSCardSerializer` via `addSerializer(...)`. `JSCardGroupSerializer` is left as an opt-in registration because both `JSCardSerializer` and `JSCardGroupSerializer` operate on the same `Entity` type — consumers needing the CardGroup output register it explicitly.
- [x] 6.2 Verify the module exports stay correct in `src/main/java/module-info.java` — no new packages need exporting (everything stays under `org.mnode.ical4j.serializer.jmap`, which is already exported).
- [x] 6.3 Add a top-of-package Javadoc on `JSCalendarModule` listing the supported subset and explicit known limitations (deferred items from this change).
- [ ] 6.4 Add an end-to-end Spock spec that registers `JSCalendarModule` on an `ObjectMapper` and round-trips a multi-component fixture, asserting the produced JSON matches a checked-in `.json` golden file under `src/test/resources/samples/jscalendar/`. *(Follow-up — see §7.)*

## 7. Fixtures and documentation

- [ ] 7.1 Add fixtures under `src/test/resources/samples/jscalendar/`: at minimum `event-utc.ics`/`.json`, `event-zoned.ics`/`.json`, `event-allday.ics`/`.json`, `event-recurring.ics`/`.json`, `task-due.ics`/`.json`, `card-basic.vcf`/`.json`. *(Follow-up.)*
- [ ] 7.2 Update the README (or whichever doc lists serializers) to mention `JSCalendarModule`. Skip if no such doc exists today.

## 8. Quality gate

- [x] 8.1 Run `./gradlew test` — all new Spock specs pass, existing tests remain green.
- [ ] 8.2 Run `./gradlew check` (if it adds checkstyle/spotbugs beyond `test`) and resolve any new warnings introduced by this change.
- [ ] 8.3 Self-review: confirm every scenario in `specs/jscalendar-serialization/spec.md` is covered by at least one test from sections 2–6. *(Most scenarios covered by tests added in §2/§5; a handful remain as gaps tracked in §3.6/§4.4/§7.)*
