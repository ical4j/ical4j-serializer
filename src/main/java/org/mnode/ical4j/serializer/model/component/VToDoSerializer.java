package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VToDo;
import org.mnode.ical4j.serializer.model.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VToDo} objects to JSON format.
 *
 * NOTE: Model conversion to JSON is "lossy" in that child components are ignored. This is intentional as
 * model JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VToDoSerializer extends StdSerializer<VToDo> {

    /**
     * A subset of VTODO properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY", "SUMMARY");

    public VToDoSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildTodo(value));
    }

    private JsonNode buildTodo(VToDo toDo) {
        JsonObjectBuilder builder = new JsonObjectBuilder(toDo, JOT_PROPS);
        return builder.build();
    }
}
