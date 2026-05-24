package org.mnode.ical4j.serializer.jmap;

import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.parameter.TzId;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.DateProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Optional;

/**
 * Split an ical4j {@link DateProperty} into the three JSCalendar parts:
 * a localised date-time string, an optional time-zone identifier, and a
 * {@code showWithoutTime} flag for date-only values.
 *
 * <p>Per RFC 8984 §4.1.3 / §4.3.1, JSCalendar timestamps are emitted as
 * <em>local</em> ISO-8601 strings (no offset, no trailing {@code Z}), with
 * the zone carried separately as {@code timeZone}.</p>
 */
final class JSCalendarTemporal {

    private static final DateTimeFormatter LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    static final String UTC = "Etc/UTC";

    private final String localDateTime;

    private final String timeZone;

    private final boolean showWithoutTime;

    private JSCalendarTemporal(String localDateTime, String timeZone, boolean showWithoutTime) {
        this.localDateTime = localDateTime;
        this.timeZone = timeZone;
        this.showWithoutTime = showWithoutTime;
    }

    String localDateTime() {
        return localDateTime;
    }

    String timeZone() {
        return timeZone;
    }

    boolean showWithoutTime() {
        return showWithoutTime;
    }

    static JSCalendarTemporal from(DateProperty<? extends Temporal> property) {
        Optional<TzId> tzId = property.getParameter(Parameter.TZID);
        boolean dateOnly = property.getParameters("VALUE").contains(Value.DATE);
        Temporal value = property.getDate();
        if (dateOnly || value instanceof LocalDate) {
            return forLocalDate(value);
        }
        if (value instanceof Instant) {
            LocalDateTime ldt = LocalDateTime.ofInstant((Instant) value, ZoneOffset.UTC);
            return new JSCalendarTemporal(LOCAL.format(ldt), UTC, false);
        }
        if (value instanceof ZonedDateTime) {
            ZonedDateTime zdt = (ZonedDateTime) value;
            // Prefer the original TZID parameter value where set — ical4j wraps unknown
            // zones with a synthetic id, but the raw param preserves the iCal source.
            String zone = tzId.map(TzId::getValue).orElseGet(() -> zdt.getZone().getId());
            return new JSCalendarTemporal(LOCAL.format(zdt.toLocalDateTime()), zone, false);
        }
        if (value instanceof OffsetDateTime) {
            OffsetDateTime odt = (OffsetDateTime) value;
            String zone = ZoneOffset.UTC.equals(odt.getOffset()) ? UTC : odt.getOffset().getId();
            return new JSCalendarTemporal(LOCAL.format(odt.toLocalDateTime()), zone, false);
        }
        if (value instanceof LocalDateTime) {
            String zone = tzId.map(TzId::getValue).orElse(null);
            return new JSCalendarTemporal(LOCAL.format((LocalDateTime) value), zone, false);
        }
        // fall back to property's string value
        return new JSCalendarTemporal(property.getValue(), tzId.map(TzId::getValue).orElse(null), false);
    }

    private static JSCalendarTemporal forLocalDate(Temporal value) {
        LocalDate date;
        if (value instanceof LocalDate) {
            date = (LocalDate) value;
        } else if (value instanceof LocalDateTime) {
            date = ((LocalDateTime) value).toLocalDate();
        } else if (value instanceof ZonedDateTime) {
            date = ((ZonedDateTime) value).toLocalDate();
        } else if (value instanceof Instant) {
            date = LocalDateTime.ofInstant((Instant) value, ZoneOffset.UTC).toLocalDate();
        } else {
            date = LocalDate.parse(value.toString());
        }
        return new JSCalendarTemporal(date.atStartOfDay().format(LOCAL), null, true);
    }

    /**
     * Convenience for serializing UTC instants (e.g. CREATED/LAST-MODIFIED) as
     * the canonical JSCalendar UTC string ending in {@code Z}.
     */
    static String formatUtc(Instant instant) {
        return instant.toString();
    }
}
