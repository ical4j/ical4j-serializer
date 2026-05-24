## Context

`org.mnode.ical4j.serializer.jmap` currently ships skeleton serializers for the JSCalendar / JSContact JSON formats. Each `build()` method returns the result of `AbstractJSCalendarBuilder.createObjectNode()` directly — an `ObjectNode` containing only `{"@type": "<typename>"}`. The wiring (Jackson `StdSerializer` subclasses, builder hierarchy, constructor parameters) is in place; only the body of every `build()` method needs filling in.

JSCalendar (RFC 8984) is structurally close to iCalendar but differs in several specific ways:

- temporal values are ISO 8601 strings (`"2024-05-16T09:00:00"`), with the time zone carried in a sibling `timeZone` property — not bundled into the timestamp.
- durations are ISO 8601 (`"PT1H30M"`).
- many top-level iCalendar properties (`ATTENDEE`, `LOCATION`, `VALARM`, `CONFERENCE`) become structured sub-objects keyed by an arbitrary id, not flat arrays.
- recurrence is modelled as `recurrenceRules` (array of structured `RecurrenceRule` objects) plus `recurrenceOverrides` (a map of override patches).

The companion jCal serializers (in the package root) take a different shape: `JCalSerializer` walks the calendar with a switch statement that maps each property name to a jCal type token (`"date-time"`, `"duration"`, …) and emits four-element arrays. That code is a useful reference for the property enumeration pattern, but its temporal encoding is jCal-specific and is not reusable verbatim for JSCalendar.

The existing `JsonBuilder` interface provides helpers (`putIfNotAbsent`, `putIfNotNull`, `encodeValue`) that JSCalendar builders can reuse for the simple scalar properties.

## Goals / Non-Goals

**Goals:**
- Emit RFC 8984-conformant JSON for `VEvent` (`jsevent`), `VToDo` (`jstask`), and `VJournal` (treated as a `jsevent` with `freeBusyStatus: "free"` per RFC 8984 §5.1.4 conventions), so a JSCalendar consumer can round-trip the common fields produced by ical4j-driven applications.
- Emit RFC 9610-conformant JSON for `VCard` (`Card`) and `Group` (`CardGroup`).
- Produce JSON that Jackson can deserialize back into `JsonNode`/`Map` cleanly, with field names matching the RFC exactly (camelCase, `@type` discriminator first).
- Keep each builder small and composable: one builder class per JSCalendar object type, drawing on shared property-mapping helpers.
- Provide a `JSCalendarModule extends SimpleModule` so consumers can register all six serializers with `mapper.registerModule(new JSCalendarModule())`.

**Non-Goals:**
- Deserialization (JSCalendar → ical4j) — out of scope for this change.
- 100% RFC 8984 coverage. Properties not in the core mapping list (e.g. `useDefaultAlerts`, `localizations`, `byDay` second-order rule parts) can be added incrementally; this change establishes the framework and covers the high-value subset.
- Refactoring the `jotn` package or the ActivityStream / RDF builders currently in flight on `develop`.
- Performance work: a single Jackson `ObjectMapper` allocation per `build()` call is acceptable for the initial implementation (matching the existing pattern in `AbstractJSCalendarBuilder.createObjectNode`).

## Decisions

### D1: Complete the existing `jmap` package rather than starting a new one

**Decision**: Build on the in-place stubs (`JSEventSerializer`, `AbstractJSCalendarBuilder`, `LocationBuilder`, `LinkBuilder`, `VirtualLocationBuilder`) instead of introducing a parallel package.

**Rationale**: The class names, package path, `@type` token values (`"jsevent"`, `"jstask"`, `"Card"`, `"CardGroup"`), and module exports already match RFC 8984 / RFC 9610 naming. The scaffold was written with this format in mind; replacing it would be churn for no gain.

**Alternatives considered**:
- *New `jscalendar/` package alongside `jmap/`* — rejected: leaves dead stubs in `jmap/` and duplicates the module export surface.
- *Extend the `jotn` package* — rejected: `jotn` is the separate "jot notation" format (see commits `84549c5`, `20c78c7`), not JSCalendar.

### D2: One builder class per JSCalendar object type, mirroring the StdSerializer

**Decision**: Each `StdSerializer` (`JSEventSerializer`, `JSTaskSerializer`, …) owns a static-nested `Builder` extending `AbstractJSCalendarBuilder<T>`, as already established by `JSEventSerializer.JSEventBuilder`. The builder's `build()` method is the only place that knows the property→JSON mapping for that object type.

**Rationale**: Matches the existing scaffold and keeps the serializer↔builder split that lets builders be reused from outside Jackson (e.g. for assembling a parent `Calendar` document that nests events).

**Alternatives considered**:
- *Single mega-builder with a type switch* — rejected: makes the per-component property mappings hard to read and conflicts with the existing one-class-per-type stubs.

### D3: Property mapping via small per-property helpers, not a giant switch

**Decision**: Introduce a `JSCalendarPropertyMapper` (or a set of static methods on `AbstractJSCalendarBuilder`) that handles the common property-to-field conversions: `Uid` → `uid`, `Summary` → `title`, `Description` → `description`, `Created` → `created`, `LastModified` → `updated`, `DtStart` + `Duration`/`DtEnd` → `start`/`duration`/`timeZone`, `Status` → `status` (with iCalendar→JSCalendar value remapping), `Priority` → `priority`, `Sequence` → `sequence`, `Categories` → `keywords`, etc.

**Rationale**: Most properties have a 1-to-1 trivial mapping. Co-locating those keeps each builder's `build()` body short ("call helper for the common stuff, then add object-specific fields"). The non-trivial ones (`DtStart`, `RRule`, `Attendee`) deserve their own helpers so each one fits on a screen.

**Alternatives considered**:
- *Mirror `JCalSerializer.getPropertyType`* — rejected: that switch returns a *type token* for the jCal array format; JSCalendar needs both a *field name* and a *value shape*, so a single switch is the wrong shape.

### D4: Time-zone handling — split `DtStart` into `start` + `timeZone`, ignore `TZID` parameter when value is UTC

**Decision**: When serializing `DtStart`/`Due`:
- If the value is a UTC `Instant`, emit `start: "<localised>"` and `timeZone: "Etc/UTC"`.
- If the value is a zoned date-time, emit `start: "<local part>"` and `timeZone: "<TZID>"`.
- If the value is a floating date-time (no TZID), emit `start: "<local>"` and omit `timeZone`.
- Date-only values produce `start: "<date>T00:00:00"` and `showWithoutTime: true`.

**Rationale**: Matches RFC 8984 §4.3.1 and §4.1.3. This is the one piece of the conversion that genuinely doesn't have a generic helper — it needs to inspect the ical4j `Temporal` type. Encoding this as one cohesive `applyStartAndDuration(...)` method keeps the awkwardness in one place.

**Risks**: ical4j 4.2 uses `java.time.temporal.Temporal` polymorphically (`LocalDate`, `LocalDateTime`, `OffsetDateTime`, `ZonedDateTime`, `Instant`); the helper needs a small `instanceof`-cascade. Tested via fixture cases covering each variant.

### D5: Recurrence — emit `recurrenceRules` array, defer overrides to a follow-up

**Decision**: Map a single `RRULE` to a one-element `recurrenceRules` array, with structured fields (`frequency`, `interval`, `byMonth`, `byDay`, `byMonthDay`, `byYearDay`, `count`, `until`). `RDATE`/`EXDATE` map to `recurrenceOverrides` keys with `excluded: true` for exdates. Full override-patch support (modified instances via `RECURRENCE-ID`) is listed as a follow-up in `tasks.md`.

**Rationale**: A complete override implementation needs a second pass over the calendar to find sibling `VEvent`s with matching `UID` + `RECURRENCE-ID`, which exceeds the scope of a single component serializer. The recurrence-rule mapping alone covers the majority of real-world recurring events.

**Alternatives considered**:
- *Emit recurrence as a raw `RRULE:...` string* — rejected: not RFC 8984 conformant.
- *Skip recurrence entirely in this change* — rejected: too large an omission given recurring events are common.

### D6: Jackson module for one-shot registration

**Decision**: Add `JSCalendarModule extends com.fasterxml.jackson.databind.module.SimpleModule` in the `jmap` package that calls `addSerializer(...)` for each of the six concrete serializers. Consumers register it with `mapper.registerModule(new JSCalendarModule())`.

**Rationale**: Mirrors how the jCal/xCal serializers are typically wired (see `JCalSerializer` constructors), gives consumers one symbol to remember, and avoids requiring them to know the full list of `StdSerializer` subclasses.

### D7: Test strategy — golden-file fixtures under `samples/jscalendar/`

**Decision**: For each serializer, add a Spock spec that loads an `.ics` (or `.vcf`) fixture, runs the serializer, and asserts the produced JSON equals a checked-in `.json` fixture (compared as parsed `JsonNode`, not raw strings, to avoid whitespace/order false negatives — Jackson sorts `ObjectNode` insertion order, so order assertions are stable).

**Rationale**: Matches the existing `samples/jcal/{1.ics,1.json}` convention. Keeps the expected output reviewable in the PR diff.

## Risks / Trade-offs

- **RFC 8984 coverage is intentionally partial** → mitigation: document the supported property subset in package-level Javadoc on `JSCalendarModule`; track gaps as follow-up tasks rather than tickets, so future contributors find them.
- **Time-zone conversion edge cases** (custom `VTIMEZONE` definitions, deprecated Olson aliases) → mitigation: pass TZID through as-is when it matches the iCalendar source; only normalise the explicit UTC case. Document this as a known limitation.
- **Behaviour change for consumers depending on the empty stub** → mitigation: call this out in the proposal's BREAKING note and in the release notes when the change ships. The stub returned non-conformant JSON anyway (only `@type`, no other fields), so we judge real-world breakage near-zero.
- **Recurrence overrides deferred** → mitigation: `RDATE`/`EXDATE` *are* covered (those don't need cross-component lookups); only modified-instance overrides (`RECURRENCE-ID`) are deferred. The omission is explicit in the docs and tracked as a follow-up task in `tasks.md`.
- **Larger `build()` bodies than the current stubs** → mitigation: factor the common property mappings into helpers per D3 so no single `build()` exceeds ~60 lines.

## Migration Plan

Not applicable — this is an additive change behind an opt-in Jackson module. Consumers that don't register `JSCalendarModule` see no change. Consumers that previously used the stub serializers receive richer output, which is the intended improvement (see proposal BREAKING note).

## Open Questions

- Should `VJournal` map to `jsevent` (with `freeBusyStatus: "free"`) or to a custom non-standard `@type`? RFC 8984 has no first-class journal entry. Tentative answer: `jsevent` with `freeBusyStatus: "free"` and `showWithoutTime: true` to match the journal semantics; revisit if a user complains.
- Should the `JSCalendarModule` also register a top-level `Calendar` → `Group`-of-`jsevent` serializer, or only the per-component ones? Tentative answer: leave the top-level wrapper out of this change; consumers wanting it can compose `mapper.valueToTree(event)` themselves.
