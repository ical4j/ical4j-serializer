package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VJournal;

import java.io.IOException;

/**
 * Serializer for JMAP JSEvent objects, which represent calendar events.
 * This class extends StdSerializer to provide custom serialization logic for VEvent components.
 */
public class JSEventSerializer extends StdSerializer<VEvent> {

    public JSEventSerializer() {
        super(VEvent.class);
    }

    public JSEventSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    public void serialize(VEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSEvent(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildJSEvent(VEvent event) throws ConstraintViolationException {
        return new JSEventBuilder().component(event).build();
    }

    public static class JSEventBuilder extends AbstractJSCalendarBuilder<VEvent> {

        public JSEventBuilder() {
            super("jsevent");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsEvent = createObjectNode();
            JSCalendarPropertyMapper.applyUid(component, jsEvent);
            JSCalendarPropertyMapper.applyTitle(component, jsEvent);
            JSCalendarPropertyMapper.applyDescription(component, jsEvent);
            JSCalendarPropertyMapper.applyTimestamps(component, jsEvent);
            JSCalendarPropertyMapper.applySequence(component, jsEvent);
            JSCalendarPropertyMapper.applyPriority(component, jsEvent);
            JSCalendarPropertyMapper.applyStatus(component, jsEvent);
            JSCalendarPropertyMapper.applyCategoriesAsKeywords(component, jsEvent);
            JSCalendarPropertyMapper.applyColor(component, jsEvent);
            JSCalendarPropertyMapper.applyPrivacy(component, jsEvent);
            JSCalendarPropertyMapper.applyLinkFromUrl(component, jsEvent);
            applyStartAndDuration(component, jsEvent);
            ParticipantBuilder.applyTo(component, jsEvent);
            LocationBuilder.applyTo(component, jsEvent);
            VirtualLocationBuilder.applyTo(component, jsEvent);
            AlertBuilder.applyTo(component, jsEvent);
            RecurrenceRuleBuilder.applyTo(component, jsEvent);
            return jsEvent;
        }
    }

    /**
     * Variant builder used for {@code VJournal} components — JSCalendar has no
     * first-class journal type, so we emit a {@code jsevent} with a
     * {@code free} free/busy status (RFC 8984 §5.1.4 convention).
     */
    public static class JSJournalBuilder extends AbstractJSCalendarBuilder<VJournal> {

        public JSJournalBuilder() {
            super("jsevent");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsEvent = createObjectNode();
            JSCalendarPropertyMapper.applyUid(component, jsEvent);
            JSCalendarPropertyMapper.applyTitle(component, jsEvent);
            JSCalendarPropertyMapper.applyDescription(component, jsEvent);
            JSCalendarPropertyMapper.applyTimestamps(component, jsEvent);
            JSCalendarPropertyMapper.applySequence(component, jsEvent);
            JSCalendarPropertyMapper.applyStatus(component, jsEvent);
            JSCalendarPropertyMapper.applyCategoriesAsKeywords(component, jsEvent);
            JSCalendarPropertyMapper.applyColor(component, jsEvent);
            JSCalendarPropertyMapper.applyPrivacy(component, jsEvent);
            JSCalendarPropertyMapper.applyLinkFromUrl(component, jsEvent);
            applyStartAndDuration(component, jsEvent);
            jsEvent.put("freeBusyStatus", "free");
            return jsEvent;
        }
    }
}
