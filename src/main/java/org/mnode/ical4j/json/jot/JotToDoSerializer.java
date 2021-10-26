package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.IOException;

/**
 * Convert iCal4j {@link VToDo} objects to Jot JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class JotToDoSerializer extends StdSerializer<VToDo> {

    public JotToDoSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildTodo(value));
    }

    private JsonNode buildTodo(VToDo toDo) {
        AbstractJotBuilder<VToDo> builder = new ComponentBuilder<VToDo>().component(toDo);
        return builder.build();
    }
}
