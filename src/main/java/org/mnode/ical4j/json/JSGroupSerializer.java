package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ConstraintViolationException;

import java.io.IOException;

public class JSGroupSerializer extends AbstractJSSerializer<Calendar> {

    public JSGroupSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSGroup(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
