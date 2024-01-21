package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VResource;
import org.mnode.ical4j.serializer.jotn.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VResource} objects to JSON format.
 */
public class VResourceSerializer extends StdSerializer<VResource> {

    /**
     * A subset of VRESOURCE properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public VResourceSerializer(Class<VResource> t) {
        super(t);
    }

    @Override
    public void serialize(VResource value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildResource(value));
    }

    private JsonNode buildResource(VResource resource) {
        JsonObjectBuilder builder = new JsonObjectBuilder(resource, JOT_PROPS);
        return builder.build();
    }
}
