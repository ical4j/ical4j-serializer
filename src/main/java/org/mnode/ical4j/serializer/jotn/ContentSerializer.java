package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.fortuna.ical4j.model.PropertyContainer;

import java.io.IOException;
import java.util.List;

public class ContentSerializer<T extends PropertyContainer> extends JsonSerializer<T> {

    private final List<String> propertyNames;

    public ContentSerializer(List<String> propertyNames) {
        this.propertyNames = propertyNames;
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeTree(new JsonObjectBuilder(t, propertyNames).build());
    }
}
