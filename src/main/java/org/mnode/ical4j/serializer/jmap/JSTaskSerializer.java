package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.IOException;

public class JSTaskSerializer extends StdSerializer<VToDo> {

    public JSTaskSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSTask(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildJSTask(VToDo toDo) throws ConstraintViolationException {
        AbstractJSCalendarBuilder<VToDo> builder = new JSTaskBuilder().component(toDo);
        return builder.build();
    }

    public static class JSTaskBuilder extends AbstractJSCalendarBuilder<VToDo> {

        public JSTaskBuilder() {
            super("jstask");
        }

        @Override
        public JsonNode build() {
            var jsTask = createObjectNode();
            return jsTask;
        }
    }
}
