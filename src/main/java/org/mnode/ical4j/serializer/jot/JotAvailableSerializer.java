package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.Available;

import java.io.IOException;

/**
 * Convert iCal4j {@link Available} objects to Jot JSON format.
 */
public class JotAvailableSerializer extends StdSerializer<Available> {

    public JotAvailableSerializer(Class<Available> t) {
        super(t);
    }

    @Override
    public void serialize(Available value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAvailable(value));
    }

    private JsonNode buildAvailable(Available available) {
        AbstractJotBuilder<Available> builder = new ComponentBuilder<Available>().component(available);
        return builder.build();
    }
}
