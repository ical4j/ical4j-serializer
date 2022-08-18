package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.VFreeBusy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class JotFreeBusyMapper extends AbstractJotCalMapper<VFreeBusy> {

    public JotFreeBusyMapper(Class<VFreeBusy> t) {
        super(t);
    }

    @Override
    public VFreeBusy deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VFreeBusy freeBusy = new VFreeBusy(false);
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    freeBusy.addAll(parsePropertyList(propertyName, p));
                } else {
                    freeBusy.add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return freeBusy;
    }
}
