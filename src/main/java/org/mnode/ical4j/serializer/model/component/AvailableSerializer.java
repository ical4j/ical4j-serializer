package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.Available;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link Available} objects to JSON format.
 */
public class AvailableSerializer extends StdSerializer<Available> {

    public AvailableSerializer(Class<Available> t) {
        super(t);
    }

    @Override
    public void serialize(Available value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAvailable(value));
    }

    private JsonNode buildAvailable(Available available) {
        AbstractJsonBuilder<Available> builder = new ComponentJsonBuilder<Available>().component(available);
        return builder.build();
    }
}
