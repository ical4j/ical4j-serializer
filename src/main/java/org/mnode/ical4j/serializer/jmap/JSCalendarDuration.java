package org.mnode.ical4j.serializer.jmap;

import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

/**
 * Helpers for producing the JSCalendar {@code duration} ISO-8601 string from
 * either an iCalendar {@code DURATION} property or a {@code DTSTART}/{@code DTEND}
 * pair.
 */
final class JSCalendarDuration {

    private JSCalendarDuration() {
    }

    static String from(Duration duration) {
        return format(duration.getDuration());
    }

    static String between(DtStart<? extends Temporal> start, DtEnd<? extends Temporal> end) {
        java.time.Duration d = java.time.Duration.between(start.getDate(), end.getDate());
        return format(d);
    }

    private static String format(TemporalAmount amount) {
        // java.time.Duration's toString() returns the canonical ISO-8601 PnDTnHnMnS form,
        // which is exactly what JSCalendar expects.
        return amount.toString();
    }
}
