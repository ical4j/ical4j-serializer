package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.Property;

import java.io.IOException;

public class JSCalendarSerializer extends StdSerializer<Calendar> {

    public JSCalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // For calendar objects with a UID we assume a JSGroup is used to represent in jscalendar..
        if (value.getProperties().getFirst(Property.UID).isPresent()) {
            try {
                gen.writeTree(buildJSGroup(value));
            } catch (ConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        }
        // For calendar objects with one or more VEVENTs assume a JSEvent representation..
        else if (value.getComponents().getFirst(Component.VEVENT).isPresent()) {
            try {
                gen.writeTree(buildJSEvent(value));
            } catch (ConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        }
        // For calendar objects with one or more VTODOs assume a JSTask representation..
        else if (value.getComponents().getFirst(Component.VTODO).isPresent()) {
            try {
                gen.writeTree(buildJSTask(value));
            } catch (ConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Unable to produce jscalendar object from provided input");
        }
    }

    private JsonNode buildJSGroup(Calendar calendar) throws ConstraintViolationException {
        JSGroupBuilder builder = new JSGroupBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        return builder.build();
    }

    private JsonNode buildJSEvent(Calendar calendar) throws ConstraintViolationException {
        JSEventBuilder builder = new JSEventBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        return builder.build();
    }

    private JsonNode buildJSTask(Calendar calendar) throws ConstraintViolationException {
        JSTaskBuilder builder = new JSTaskBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        return builder.build();
    }
}
