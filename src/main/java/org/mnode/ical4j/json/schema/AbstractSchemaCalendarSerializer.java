package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Component;

import java.io.IOException;

public abstract class AbstractSchemaCalendarSerializer<T extends Component> extends StdSerializer<T> {

    public AbstractSchemaCalendarSerializer(Class<T> t) {
        super(t);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildSchema(value));
    }

    protected abstract JsonNode buildSchema(T component);
}
