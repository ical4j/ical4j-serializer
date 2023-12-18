package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.IOException;

public class JSEventSerializer extends StdSerializer<VEvent> {

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
        AbstractJSCalendarBuilder<VEvent> builder = new JSEventBuilder().component(event);
        return builder.build();
    }

    public static class JSEventBuilder extends AbstractJSCalendarBuilder<VEvent> {

        public JSEventBuilder() {
            super("jsevent");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsEvent = createObjectNode();
            return jsEvent;
        }
    }
}
