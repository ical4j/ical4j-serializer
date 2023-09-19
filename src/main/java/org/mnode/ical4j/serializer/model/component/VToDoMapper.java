package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.VToDo;
import org.mnode.ical4j.serializer.model.AbstractCalendarMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class VToDoMapper extends AbstractCalendarMapper<VToDo> {

    public VToDoMapper(Class<VToDo> t) {
        super(t);
    }

    @Override
    public VToDo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VToDo toDo = new VToDo(false);
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    toDo.addAll(parsePropertyList(propertyName, p));
                } else {
                    toDo.add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return toDo;
    }
}
