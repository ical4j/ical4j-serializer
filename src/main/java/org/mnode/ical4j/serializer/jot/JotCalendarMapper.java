package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class JotCalendarMapper extends AbstractJotCalMapper<Calendar> {

    public JotCalendarMapper(Class<Calendar> t) {
        super(t);
    }

    @Override
    public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Calendar calendar = new Calendar();
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    calendar.getProperties().addAll(parsePropertyList(propertyName, p));
                } else {
                    calendar.getProperties().add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return calendar;
    }
}
