package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.IOException;

public class JSTaskSerializer extends AbstractJSSerializer<VToDo> {

    public JSTaskSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSTask(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
