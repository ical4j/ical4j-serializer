package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Month;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.WeekDay;
import net.fortuna.ical4j.model.property.ExDate;
import net.fortuna.ical4j.model.property.RDate;
import net.fortuna.ical4j.model.property.RRule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Builds the JSCalendar {@code recurrenceRules} array and
 * {@code recurrenceOverrides} map from {@code RRULE}, {@code RDATE}, and
 * {@code EXDATE} properties.
 */
final class RecurrenceRuleBuilder {

    private RecurrenceRuleBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        applyRules(source, target);
        applyOverrides(source, target);
    }

    private static void applyRules(PropertyContainer source, ObjectNode target) {
        Optional<RRule<Temporal>> rrule = source.getProperty("RRULE");
        if (rrule.isEmpty()) {
            return;
        }
        ArrayNode rules = target.arrayNode();
        rules.add(buildRule(rrule.get().getRecur(), target));
        target.set("recurrenceRules", rules);
    }

    private static ObjectNode buildRule(Recur<? extends Temporal> recur, ObjectNode parent) {
        ObjectNode node = parent.objectNode();
        node.put("@type", "RecurrenceRule");
        if (recur.getFrequency() != null) {
            node.put("frequency", recur.getFrequency().name().toLowerCase(Locale.ROOT));
        }
        if (recur.getInterval() > 0 && recur.getInterval() != 1) {
            node.put("interval", recur.getInterval());
        }
        if (recur.getCount() > 0) {
            node.put("count", recur.getCount());
        }
        if (recur.getUntil() != null) {
            node.put("until", formatTemporal(recur.getUntil()));
        }
        addIntArray(node, "byMonthDay", recur.getMonthDayList());
        addMonthArray(node, "byMonth", recur.getMonthList());
        addWeekDayArray(node, "byDay", recur.getDayList(), parent);
        addIntArray(node, "byYearDay", recur.getYearDayList());
        addIntArray(node, "bySetPosition", recur.getSetPosList());
        return node;
    }

    private static void addIntArray(ObjectNode node, String field, List<Integer> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        ArrayNode array = node.arrayNode();
        for (Integer v : values) {
            array.add(v);
        }
        node.set(field, array);
    }

    private static void addMonthArray(ObjectNode node, String field, List<Month> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        ArrayNode array = node.arrayNode();
        for (Month v : values) {
            array.add(String.valueOf(v.getMonthOfYear()));
        }
        node.set(field, array);
    }

    private static void addWeekDayArray(ObjectNode node, String field, List<WeekDay> values, ObjectNode parent) {
        if (values == null || values.isEmpty()) {
            return;
        }
        ArrayNode array = node.arrayNode();
        for (WeekDay wd : values) {
            ObjectNode item = parent.objectNode();
            item.put("@type", "NDay");
            item.put("day", wd.getDay().name().toLowerCase(Locale.ROOT));
            if (wd.getOffset() != 0) {
                item.put("nthOfPeriod", wd.getOffset());
            }
            array.add(item);
        }
        node.set(field, array);
    }

    private static void applyOverrides(PropertyContainer source, ObjectNode target) {
        List<RDate<Temporal>> rdates = source.getProperties("RDATE");
        List<ExDate<Temporal>> exdates = source.getProperties("EXDATE");
        if (rdates.isEmpty() && exdates.isEmpty()) {
            return;
        }
        ObjectNode overrides = target.objectNode();
        for (RDate<Temporal> rdate : rdates) {
            for (Temporal date : rdate.getDates()) {
                overrides.set(formatTemporal(date), target.objectNode());
            }
        }
        for (ExDate<Temporal> exdate : exdates) {
            for (Temporal date : exdate.getDates()) {
                ObjectNode entry = target.objectNode();
                entry.put("excluded", true);
                overrides.set(formatTemporal(date), entry);
            }
        }
        if (!overrides.isEmpty()) {
            target.set("recurrenceOverrides", overrides);
        }
    }

    private static String formatTemporal(Temporal value) {
        DateTimeFormatter local = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay().format(local);
        }
        if (value instanceof LocalDateTime) {
            return local.format((LocalDateTime) value);
        }
        if (value instanceof ZonedDateTime) {
            return local.format(((ZonedDateTime) value).toLocalDateTime());
        }
        if (value instanceof OffsetDateTime) {
            return local.format(((OffsetDateTime) value).toLocalDateTime());
        }
        if (value instanceof java.time.Instant) {
            return local.format(((java.time.Instant) value).atZone(ZoneOffset.UTC).toLocalDateTime());
        }
        return value.toString();
    }
}
