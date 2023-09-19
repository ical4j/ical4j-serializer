package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VFreeBusy;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link VFreeBusy} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VFreeBusySerializer extends StdSerializer<VFreeBusy> {

    public VFreeBusySerializer(Class<VFreeBusy> t) {
        super(t);
    }

    @Override
    public void serialize(VFreeBusy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildFreebusy(value));
    }

    private JsonNode buildFreebusy(VFreeBusy freeBusy) {
        AbstractJsonBuilder<VFreeBusy> builder = new ComponentJsonBuilder<VFreeBusy>().component(freeBusy);
        return builder.build();
    }
}
