package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Due;
import net.fortuna.ical4j.model.property.Duration;
import org.mnode.ical4j.serializer.JsonBuilder;

import java.time.temporal.Temporal;
import java.util.Optional;

/**
 * Abstract base class for building JSCalendar objects in JSON format.
 * This class provides a common structure for calendar builders that
 * serialize components into JSON nodes.
 *
 * @param <T> the type of component to be serialized
 */
public abstract class AbstractJSCalendarBuilder<T> implements JsonBuilder {

    private final String objectType;

    protected T component;

    public AbstractJSCalendarBuilder(String objectType) {
        this.objectType = objectType;
    }

    public AbstractJSCalendarBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        var mapper = new ObjectMapper();

        var node = mapper.createObjectNode();
        node.put("@type", objectType);
        return node;
    }

    /**
     * Apply the JSCalendar start/duration/timeZone/showWithoutTime fields for an
     * event-style component (uses {@code DTSTART} for the start anchor).
     */
    protected void applyStartAndDuration(PropertyContainer source, ObjectNode target) {
        Optional<DtStart<Temporal>> dtStart = source.getProperty("DTSTART");
        dtStart.ifPresent(value -> applyTemporal(value, "start", target));

        Optional<Duration> duration = source.getProperty("DURATION");
        if (duration.isPresent()) {
            target.put("duration", JSCalendarDuration.from(duration.get()));
        } else if (dtStart.isPresent()) {
            Optional<DtEnd<Temporal>> dtEnd = source.getProperty("DTEND");
            dtEnd.ifPresent(end -> target.put("duration", JSCalendarDuration.between(dtStart.get(), end)));
        }
    }

    /**
     * Apply the JSCalendar due/duration/timeZone fields for a task component
     * (uses {@code DUE} for the deadline anchor, plus DTSTART for the optional start).
     */
    protected void applyDueAndStart(PropertyContainer source, ObjectNode target) {
        Optional<DtStart<Temporal>> dtStart = source.getProperty("DTSTART");
        dtStart.ifPresent(value -> applyTemporal(value, "start", target));

        Optional<Due<Temporal>> due = source.getProperty("DUE");
        due.ifPresent(value -> applyTemporal(value, "due", target));

        Optional<Duration> duration = source.getProperty("DURATION");
        duration.ifPresent(value -> target.put("duration", JSCalendarDuration.from(value)));
    }

    private void applyTemporal(net.fortuna.ical4j.model.property.DateProperty<? extends Temporal> property,
                               String fieldName, ObjectNode target) {
        JSCalendarTemporal temporal = JSCalendarTemporal.from(property);
        target.put(fieldName, temporal.localDateTime());
        if (temporal.timeZone() != null && !target.has("timeZone")) {
            target.put("timeZone", temporal.timeZone());
        }
        if (temporal.showWithoutTime() && !target.has("showWithoutTime")) {
            target.put("showWithoutTime", true);
        }
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
