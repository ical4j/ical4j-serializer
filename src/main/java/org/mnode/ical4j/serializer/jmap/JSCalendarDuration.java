package org.mnode.ical4j.serializer.jmap;

import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
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
        Temporal startDate = start.getDate();
        Temporal endDate = end.getDate();
        if (startDate instanceof LocalDate && endDate instanceof LocalDate) {
            // date-only values have no time component, so express the span in nominal days..
            return format(Period.ofDays((int) ChronoUnit.DAYS.between(startDate, endDate)));
        }
        return format(java.time.Duration.between(startDate, endDate));
    }

    /**
     * Format a duration as a JSCalendar (Signed)Duration. The java.time toString() forms are ISO-8601,
     * but negative amounts are rendered with per-field signs (e.g. "PT-15M") whereas JSCalendar
     * requires a single leading sign (e.g. "-PT15M").
     */
    static String format(TemporalAmount amount) {
        if (amount instanceof java.time.Duration && ((java.time.Duration) amount).isNegative()) {
            return "-" + ((java.time.Duration) amount).negated();
        }
        if (amount instanceof Period) {
            Period period = (Period) amount;
            if (period.getYears() <= 0 && period.getMonths() <= 0 && period.getDays() <= 0 && period.isNegative()) {
                return "-" + period.negated();
            }
        }
        return amount.toString();
    }
}
