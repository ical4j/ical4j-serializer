package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.Available;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class JotAvailableMapper extends AbstractJotCalMapper<Available> {

    public JotAvailableMapper(Class<Available> t) {
        super(t);
    }

    @Override
    public Available deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Available available = new Available();
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    available.getProperties().addAll(parsePropertyList(propertyName, p));
                } else {
                    available.getProperties().add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return available;
    }
}
