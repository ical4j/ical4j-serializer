package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VTimeZone;
import org.mnode.ical4j.serializer.jotn.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VTimeZone} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VTimeZoneSerializer extends StdSerializer<VTimeZone> {

    /**
     * A subset of VTIMEZONE properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY",
            "DTSTART", "SUMMARY", "CATEGORIES", "DESCRIPTION", "RECURRENCE-ID", "CONCEPT");

    public VTimeZoneSerializer(Class<VTimeZone> t) {
        super(t);
    }

    @Override
    public void serialize(VTimeZone value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildEvent(value));
    }

    private JsonNode buildEvent(VTimeZone event) {
        var builder = new JsonObjectBuilder(event, JOT_PROPS);
        return builder.build();
    }
}
