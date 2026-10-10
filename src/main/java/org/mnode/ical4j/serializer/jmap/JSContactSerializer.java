package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.vcard.Entity;

import java.io.IOException;

/**
 * Serializes a vCard {@link Entity} as a JSContact {@code CardGroup} when it has {@code KIND:group}, and as a
 * {@code Card} otherwise.
 */
public class JSContactSerializer extends StdSerializer<Entity> {

    private final JSCardSerializer cardSerializer = new JSCardSerializer();

    private final JSCardGroupSerializer groupSerializer = new JSCardGroupSerializer();

    public JSContactSerializer() {
        super(Entity.class);
    }

    @Override
    public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (isGroup(value)) {
            groupSerializer.serialize(value, gen, provider);
        } else {
            cardSerializer.serialize(value, gen, provider);
        }
    }

    private static boolean isGroup(Entity entity) {
        return entity.<Property>getProperty("KIND")
                .map(kind -> "group".equalsIgnoreCase(kind.getValue()))
                .orElse(false);
    }
}
