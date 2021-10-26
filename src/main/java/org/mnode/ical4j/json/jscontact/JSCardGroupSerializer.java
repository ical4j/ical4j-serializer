package org.mnode.ical4j.json.jscontact;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.vcard.VCard;

import java.io.IOException;

public class JSCardGroupSerializer extends StdSerializer<VCard> {

    public JSCardGroupSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildCardGroup(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildCardGroup(VCard card) throws ConstraintViolationException {
        AbstractJSContactBuilder builder = new JSCardGroupBuilder().component(card);
        return builder.build();
    }
}
