package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VAlarm;
import org.mnode.ical4j.serializer.jotn.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VAlarm} objects to JSON format.
 */
public class VAlarmSerializer extends StdSerializer<VAlarm> {

    /**
     * A subset of VALARM properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public VAlarmSerializer(Class<VAlarm> t) {
        super(t);
    }

    @Override
    public void serialize(VAlarm value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAlarm(value));
    }

    private JsonNode buildAlarm(VAlarm alarm) {
        var builder = new JsonObjectBuilder(alarm, JOT_PROPS);
        return builder.build();
    }
}
