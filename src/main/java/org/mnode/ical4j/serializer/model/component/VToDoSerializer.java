package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VToDo;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link VToDo} objects to JSON format.
 *
 * NOTE: Model conversion to JSON is "lossy" in that child components are ignored. This is intentional as
 * model JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VToDoSerializer extends StdSerializer<VToDo> {

    public VToDoSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildTodo(value));
    }

    private JsonNode buildTodo(VToDo toDo) {
        AbstractJsonBuilder<VToDo> builder = new ComponentJsonBuilder<VToDo>().component(toDo);
        return builder.build();
    }
}
