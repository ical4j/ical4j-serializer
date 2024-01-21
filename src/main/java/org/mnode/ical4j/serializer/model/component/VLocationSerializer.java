package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VLocation;
import org.mnode.ical4j.serializer.model.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VLocation} objects to JSON format.
 */
public class VLocationSerializer extends StdSerializer<VLocation> {

    /**
     * A subset of VLOCATION properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public VLocationSerializer(Class<VLocation> t) {
        super(t);
    }

    @Override
    public void serialize(VLocation value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildLocation(value));
    }

    private JsonNode buildLocation(VLocation location) {
        JsonObjectBuilder builder = new JsonObjectBuilder(location, JOT_PROPS);
        return builder.build();
    }
}
