package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import org.mnode.ical4j.json.schema.EventBuilder;

import java.io.IOException;

public class SchemaEventSerializer extends StdSerializer<VEvent> {

    public SchemaEventSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    public void serialize(VEvent value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildEvent(value));
    }

    private JsonNode buildEvent(VEvent event) {
        Uid uid = event.getProperty(Property.UID);
        Summary summary = event.getProperty(Property.SUMMARY);

        EventBuilder builder = new EventBuilder().uid(uid).summary(summary);
        return builder.build();
    }
}
