package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VFreeBusy;
import org.mnode.ical4j.serializer.model.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VFreeBusy} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VFreeBusySerializer extends StdSerializer<VFreeBusy> {

    /**
     * A subset of VFREEBUSY properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public VFreeBusySerializer(Class<VFreeBusy> t) {
        super(t);
    }

    @Override
    public void serialize(VFreeBusy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildFreebusy(value));
    }

    private JsonNode buildFreebusy(VFreeBusy freeBusy) {
        JsonObjectBuilder builder = new JsonObjectBuilder(freeBusy, JOT_PROPS);
        return builder.build();
    }
}
