package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public abstract class AbstractJsonLdSerializer<T> extends StdSerializer<T> {

    public AbstractJsonLdSerializer(Class<T> t) {
        super(t);
    }

    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildSchema(value));
    }

    protected abstract JsonNode buildSchema(T component);
}
