package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.vcard.VCard;

import java.io.IOException;

public class JSCardSerializer extends StdSerializer<VCard> {

    public JSCardSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildCard(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildCard(VCard card) throws ConstraintViolationException {
        AbstractJSContactBuilder builder = new JSCardBuilder().component(card);
        return builder.build();
    }

    public static class JSCardBuilder extends AbstractJSContactBuilder {

        @Override
        public JsonNode build() {
            ObjectNode card = createObjectNode();
            card.put("@type", "jsevent");
            return card;
        }
    }
}
