package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.VAlarm;
import org.mnode.ical4j.serializer.model.AbstractCalendarMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class VAlarmMapper extends AbstractCalendarMapper<VAlarm> {

    public VAlarmMapper(Class<VAlarm> t) {
        super(t);
    }

    @Override
    public VAlarm deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VAlarm alarm = new VAlarm();
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    alarm.addAll(parsePropertyList(propertyName, p));
                } else {
                    alarm.add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return alarm;
    }
}
