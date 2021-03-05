package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
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

    private final List<ComponentFactory<?>> componentFactories;

    public JCalMapper(Class<?> vc) {
        super(vc);
        parameterFactories = Arrays.asList();
        propertyFactories = Arrays.asList(new ProdId.Factory(), new Version.Factory(), new Uid.Factory());
        componentFactories = Arrays.asList(new VEvent.Factory());
    }

    @Override
    public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Calendar calendar = new Calendar();
        assertTextValue(p, "vcalendar");
        // calendar properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                calendar.add(parseProperty(p));
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
        // calendar components..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                calendar.add((CalendarComponent) parseComponent(p));
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return calendar;
    }

    private Component parseComponent(JsonParser p) throws IOException, URISyntaxException {
        assertCurrentToken(p, JsonToken.START_ARRAY);
        ComponentBuilder<?> componentBuilder = new ComponentBuilder<>().factories(componentFactories);
        componentBuilder.name(p.nextTextValue());
        // component properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            componentBuilder.property(parseProperty(p));
        }
        // sub-components..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            componentBuilder.subComponent(parseComponent(p));
        }
        return componentBuilder.build();
    }

    private Property parseProperty(JsonParser p) throws IOException, URISyntaxException {
        assertCurrentToken(p, JsonToken.START_ARRAY);
        PropertyBuilder propertyBuilder = new PropertyBuilder().factories(propertyFactories);
        propertyBuilder.name(p.nextTextValue());
        // property params..
        assertNextToken(p, JsonToken.START_OBJECT);
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
        String propertyType = p.nextTextValue();
        switch (propertyType) {
            case "binary":
                propertyBuilder.parameter(Value.BINARY);
            case "duration":
                propertyBuilder.parameter(Value.DURATION);
            case "date":
                propertyBuilder.parameter(Value.DATE);
            case "date-time":
                propertyBuilder.parameter(Value.DATE_TIME);
            case "period":
                propertyBuilder.parameter(Value.PERIOD);
            case "uri":
                propertyBuilder.parameter(Value.URI);
        }

        propertyBuilder.value(p.nextTextValue());
        assertNextToken(p, JsonToken.END_ARRAY);

        return propertyBuilder.build();
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
