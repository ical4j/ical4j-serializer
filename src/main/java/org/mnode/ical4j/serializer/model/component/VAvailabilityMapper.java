package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.VAvailability;
import org.mnode.ical4j.serializer.model.AbstractCalendarMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class VAvailabilityMapper extends AbstractCalendarMapper<VAvailability> {

    public VAvailabilityMapper(Class<VAvailability> t) {
        super(t);
    }

    @Override
    public VAvailability deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VAvailability availability = new VAvailability(false);
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    availability.addAll(parsePropertyList(propertyName, p));
                } else {
                    availability.add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return availability;
    }
}
