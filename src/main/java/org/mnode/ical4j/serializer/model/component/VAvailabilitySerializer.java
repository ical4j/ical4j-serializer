package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VAvailability;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link VAvailability} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VAvailabilitySerializer extends StdSerializer<VAvailability> {

    public VAvailabilitySerializer(Class<VAvailability> t) {
        super(t);
    }

    @Override
    public void serialize(VAvailability value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAvailability(value));
    }

    private JsonNode buildAvailability(VAvailability availability) {
        AbstractJsonBuilder<VAvailability> builder = new ComponentJsonBuilder<VAvailability>()
                .component(availability);
        return builder.build();
    }
}
