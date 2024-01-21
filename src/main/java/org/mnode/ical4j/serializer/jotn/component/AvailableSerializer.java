package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.Available;
import org.mnode.ical4j.serializer.jotn.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Available} objects to JSON format.
 */
public class AvailableSerializer extends StdSerializer<Available> {

    /**
     * A subset of AVAILABLE properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public AvailableSerializer(Class<Available> t) {
        super(t);
    }

    @Override
    public void serialize(Available value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAvailable(value));
    }

    private JsonNode buildAvailable(Available available) {
        JsonObjectBuilder builder = new JsonObjectBuilder(available, JOT_PROPS);
        return builder.build();
    }
}
