package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VAlarm;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link VAlarm} objects to JSON format.
 */
public class VAlarmSerializer extends StdSerializer<VAlarm> {

    public VAlarmSerializer(Class<VAlarm> t) {
        super(t);
    }

    @Override
    public void serialize(VAlarm value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAlarm(value));
    }

    private JsonNode buildAlarm(VAlarm alarm) {
        AbstractJsonBuilder<VAlarm> builder = new ComponentJsonBuilder<VAlarm>().component(alarm);
        return builder.build();
    }
}
