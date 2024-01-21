package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.Property;

import java.io.IOException;

public class JSGroupSerializer extends StdSerializer<Calendar> {

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
        AbstractJSCalendarBuilder<Calendar> builder = new JSGroupBuilder().component(calendar);
        return builder.build();
    }

    public static class JSGroupBuilder extends AbstractJSCalendarBuilder<Calendar> {

        public JSGroupBuilder() {
            super("jsgroup");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsGroup = createObjectNode();
            putIfNotAbsent("prodid", jsGroup, component.getProperty(Property.PRODID));
            putIfNotAbsent("uid", jsGroup, component.getProperty(Property.UID));
            return jsGroup;
        }
    }
}
