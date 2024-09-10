package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.vcard.VCard;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class VCardMapper extends AbstractVCardMapper<VCard> {

    public VCardMapper(Class<VCard> t) {
        super(t);
    }

    @Override
    public VCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var card = new VCard();
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            var propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    card.addAll(parsePropertyList(propertyName, p));
                } else {
                    card.add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException | DecoderException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return card;
    }
}
