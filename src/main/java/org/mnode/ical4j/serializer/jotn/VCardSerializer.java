package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serialize a vCard object to JSON.
 */
public class VCardSerializer extends JsonSerializer<Entity> {

    @Override
    public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCard(value));
    }

    private JsonNode buildCard(Entity entity) {
        var builder = new JsonObjectBuilder(entity,
                Arrays.stream(PropertyName.values()).map(PropertyName::toString).collect(Collectors.toList()));
        return builder.build();
    }
}
