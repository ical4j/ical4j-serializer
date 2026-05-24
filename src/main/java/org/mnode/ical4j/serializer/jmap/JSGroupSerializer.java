package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VJournal;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.IOException;

/**
 * Serializer for the JSCalendar {@code Group} wrapper. A {@link Calendar}
 * becomes a single {@code Group} object whose {@code entries} array carries the
 * serialized {@code jsevent}/{@code jstask} children.
 */
public class JSGroupSerializer extends StdSerializer<Calendar> {

    public JSGroupSerializer() {
        super(Calendar.class);
    }

    public JSGroupSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSGroup(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildJSGroup(Calendar calendar) throws ConstraintViolationException {
        return new JSGroupBuilder().component(calendar).build();
    }

    public static class JSGroupBuilder extends AbstractJSCalendarBuilder<Calendar> {

        public JSGroupBuilder() {
            super("Group");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsGroup = createObjectNode();
            component.getProperty("PRODID").ifPresent(p -> jsGroup.put("prodId", p.getValue()));
            component.getProperty("UID").ifPresent(p -> jsGroup.put("uid", p.getValue()));
            component.getProperty("NAME").ifPresent(p -> {
                ObjectNode name = jsGroup.objectNode();
                name.put("full", p.getValue());
                jsGroup.set("name", name);
            });

            ArrayNode entries = jsGroup.arrayNode();
            for (Component c : component.getComponents()) {
                if (c instanceof VEvent) {
                    entries.add(new JSEventSerializer.JSEventBuilder().component((VEvent) c).build());
                } else if (c instanceof VToDo) {
                    entries.add(new JSTaskSerializer.JSTaskBuilder().component((VToDo) c).build());
                } else if (c instanceof VJournal) {
                    entries.add(new JSEventSerializer.JSJournalBuilder().component((VJournal) c).build());
                }
            }
            if (!entries.isEmpty()) {
                jsGroup.set("entries", entries);
            }
            return jsGroup;
        }
    }
}
