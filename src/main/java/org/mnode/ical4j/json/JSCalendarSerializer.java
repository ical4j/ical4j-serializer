package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
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
                buildJSGroup(value, gen);
            } catch (ConstraintViolationException e) {
                throw new RuntimeException(e);
            }
        }
        // For calendar objects with one or more VEVENTs assume a JSEvent representation..
        else if (value.getComponents().getFirst(Component.VEVENT).isPresent()) {
            gen.writeTree(new JSEventBuilder().build());
        }
        // For calendar objects with one or more VTODOs assume a JSTask representation..
        else if (value.getComponents().getFirst(Component.VTODO).isPresent()) {
            gen.writeTree(new JSEventBuilder().build());
        } else {
            throw new IllegalArgumentException("Unable to produce jscalendar object from provided input");
        }
    }

    private void buildJSGroup(Calendar calendar, JsonGenerator gen) throws IOException, ConstraintViolationException {
        JSGroupBuilder builder = new JSGroupBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        gen.writeTree(builder.build());
    }

    private void buildJSEvent(Calendar calendar, JsonGenerator gen) throws IOException, ConstraintViolationException {
        JSEventBuilder builder = new JSEventBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        gen.writeTree(builder.build());
    }

    private void buildJSTask(Calendar calendar, JsonGenerator gen) throws IOException, ConstraintViolationException {
        JSTaskBuilder builder = new JSTaskBuilder()
                .uid(calendar.getProperties().getRequired(Property.UID).getValue());
        gen.writeTree(builder.build());
    }
}
