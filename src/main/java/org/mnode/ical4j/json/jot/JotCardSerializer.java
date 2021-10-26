package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.vcard.VCard;

import java.io.IOException;

public class JotCardSerializer extends StdSerializer<VCard> {

    public JotCardSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCard(value));
    }

    private JsonNode buildCard(VCard card) {
        AbstractJotBuilder<VCard> builder = new CardBuilder().component(card);
        return builder.build();
    }
}
