package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;

public abstract class AbstractJSSerializer<T> extends StdSerializer<T> {

    public AbstractJSSerializer(Class<T> t) {
        super(t);
    }

    protected JsonNode buildJSGroup(Calendar calendar) throws ConstraintViolationException {
        JSGroupBuilder builder = new JSGroupBuilder()
                .uid(calendar.getProperty(Property.UID).getValue());
        return builder.build();
    }

    protected JsonNode buildJSEvent(VEvent event) throws ConstraintViolationException {
        JSEventBuilder builder = new JSEventBuilder()
                .uid(event.getProperty(Property.UID).getValue());
        return builder.build();
    }

    protected JsonNode buildJSTask(VToDo toDo) throws ConstraintViolationException {
        JSTaskBuilder builder = new JSTaskBuilder()
                .uid(toDo.getProperty(Property.UID).getValue());
        return builder.build();
    }
}
