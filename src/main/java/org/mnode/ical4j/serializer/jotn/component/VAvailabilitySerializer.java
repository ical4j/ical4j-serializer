package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VAvailability;
import org.mnode.ical4j.serializer.jotn.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VAvailability} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VAvailabilitySerializer extends StdSerializer<VAvailability> {

    /**
     * A subset of VAVAILABILITY properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public VAvailabilitySerializer(Class<VAvailability> t) {
        super(t);
    }

    @Override
    public void serialize(VAvailability value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAvailability(value));
    }

    private JsonNode buildAvailability(VAvailability availability) {
        var builder = new JsonObjectBuilder(availability, JOT_PROPS);
        return builder.build();
    }
}
