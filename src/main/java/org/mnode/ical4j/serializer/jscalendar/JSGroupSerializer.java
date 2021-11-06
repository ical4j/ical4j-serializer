package org.mnode.ical4j.serializer.jscalendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ConstraintViolationException;

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
}
