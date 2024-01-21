package org.mnode.ical4j.serializer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Serialize a vCard object to JSON.
 */
public class VCardSerializer extends StdSerializer<VCard> {

    public VCardSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCard(value));
    }

    private JsonNode buildCard(VCard card) {
        JsonObjectBuilder builder = new JsonObjectBuilder(card,
                Arrays.stream(PropertyName.values()).map(PropertyName::toString).collect(Collectors.toList()));
        return builder.build();
    }
}
