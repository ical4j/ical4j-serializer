package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VAlarm;

import java.io.IOException;

public class JotAlarmSerializer extends StdSerializer<VAlarm> {

    public JotAlarmSerializer(Class<VAlarm> t) {
        super(t);
    }

    @Override
    public void serialize(VAlarm value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildAlarm(value));
    }

    private JsonNode buildAlarm(VAlarm alarm) {
        AbstractJotBuilder<VAlarm> builder = new AlarmBuilder().component(alarm);
        return builder.build();
    }
}
