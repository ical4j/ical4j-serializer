package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Uid;
import org.mnode.ical4j.json.jot.EventBuilder;

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
        Uid uid = event.getProperty(Property.UID);
        EventBuilder builder = new EventBuilder().uid(uid);

        return builder.build();
    }
}
