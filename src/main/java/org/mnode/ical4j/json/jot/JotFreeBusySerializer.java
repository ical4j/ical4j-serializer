package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VFreeBusy;

import java.io.IOException;

/**
 * Convert iCal4j {@link VFreeBusy} objects to Jot JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class JotFreeBusySerializer extends StdSerializer<VFreeBusy> {

    public JotFreeBusySerializer(Class<VFreeBusy> t) {
        super(t);
    }

    @Override
    public void serialize(VFreeBusy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildFreebusy(value));
    }

    private JsonNode buildFreebusy(VFreeBusy freeBusy) {
        AbstractJotBuilder<VFreeBusy> builder = new ComponentBuilder<VFreeBusy>().component(freeBusy);
        return builder.build();
    }
}
