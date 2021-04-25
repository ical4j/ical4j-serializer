package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VFreeBusy;
import net.fortuna.ical4j.model.property.Uid;
import org.mnode.ical4j.json.jot.FreebusyBuilder;

import java.io.IOException;

public class JotFreeBusySerializer extends StdSerializer<VFreeBusy> {

    public JotFreeBusySerializer(Class<VFreeBusy> t) {
        super(t);
    }

    @Override
    public void serialize(VFreeBusy value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildFreebusy(value));
    }

    private JsonNode buildFreebusy(VFreeBusy freeBusy) {
        Uid uid = freeBusy.getProperty(Property.UID);
        FreebusyBuilder builder = new FreebusyBuilder().uid(uid);

        return builder.build();
    }
}
