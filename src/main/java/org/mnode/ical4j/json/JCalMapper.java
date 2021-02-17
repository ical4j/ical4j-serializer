package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class JCalMapper extends StdDeserializer<Calendar> {

    private final List<ParameterFactory<?>> parameterFactories;

    private final List<PropertyFactory<?>> propertyFactories;

    public JCalMapper(Class<?> vc) {
        super(vc);
        parameterFactories = Arrays.asList();
        propertyFactories = Arrays.asList(new ProdId.Factory(), new Version.Factory(), new Uid.Factory());
    }

    @Override
    public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Calendar calendar = new Calendar();
        assertTextValue(p, "vcalendar");
        // calendar properties..
        assertNextToken(p, JsonToken.START_ARRAY);

        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.START_ARRAY);
            PropertyBuilder propertyBuilder = new PropertyBuilder().factories(propertyFactories);
            propertyBuilder.name(p.nextTextValue());
            // property params..
            assertNextToken(p, JsonToken.START_OBJECT);
            ParameterList params = new ParameterList();
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                try {
                    Parameter parameter = new ParameterBuilder().factories(parameterFactories)
                            .name(p.currentName()).value(p.getCurrentValue().toString()).build();
                    propertyBuilder.parameter(parameter);
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            // propertyType
            p.nextTextValue();
            propertyBuilder.value(p.nextTextValue());
            assertNextToken(p, JsonToken.END_ARRAY);
            try {
                calendar.add(propertyBuilder.build());
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return calendar;
    }

    private void assertNextToken(JsonParser p, JsonToken token) throws IOException {
        if (!token.equals(p.nextToken())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private void assertCurrentToken(JsonParser p, JsonToken token) {
        if (!token.equals(p.currentToken())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private void assertTextValue(JsonParser p, String value) throws IOException {
        if (!value.equals(p.nextTextValue())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }
}
