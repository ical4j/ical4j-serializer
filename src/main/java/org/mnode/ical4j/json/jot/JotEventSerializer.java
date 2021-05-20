package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.IOException;

public class JotEventSerializer extends StdSerializer<VEvent> {

    public JotEventSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    public void serialize(VEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildEvent(value));
    }

    private JsonNode buildEvent(VEvent event) {
        AbstractJotBuilder<VEvent> builder = new EventBuilder().component(event);
        return builder.build();
    }
}
