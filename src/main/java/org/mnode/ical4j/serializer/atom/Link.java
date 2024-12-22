package org.mnode.ical4j.serializer.atom;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

import java.io.IOException;

public class Link implements JsonSerializable {

    private String rel;

    private final String href;

    public Link(String href) {
        this.href = href;
    }

    public Link withRel(String rel) {
        this.rel = rel;
        return this;
    }

    @Override
    public void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        var toXmlGenerator = (ToXmlGenerator) jsonGenerator;
        toXmlGenerator.writeStartObject();

        writeAttributes(toXmlGenerator);

        toXmlGenerator.writeEndObject();
    }

    private void writeAttributes(ToXmlGenerator gen) throws IOException {
        gen.setNextIsAttribute(true);
        if (rel != null) {
            gen.writeFieldName("rel");
            gen.writeString(rel);
        }
        gen.writeFieldName("href");
        gen.writeString(href);
        gen.setNextIsAttribute(false);
    }

    @Override
    public void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
        serialize(jsonGenerator, serializerProvider);    }
}
