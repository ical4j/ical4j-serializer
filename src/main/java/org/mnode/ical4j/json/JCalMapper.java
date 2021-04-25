package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.*;
import net.fortuna.ical4j.model.parameter.Value;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class JCalMapper extends StdDeserializer<Calendar> {

    private final List<ParameterFactory<?>> parameterFactories;

    private final List<PropertyFactory<?>> propertyFactories;

    private final List<ComponentFactory<?>> componentFactories;

    public JCalMapper(Class<?> vc) {
        super(vc);
        parameterFactories = new DefaultParameterFactorySupplier().get();
        propertyFactories = new DefaultPropertyFactorySupplier().get();
        componentFactories = Arrays.asList(new Available.Factory(), new Daylight.Factory(), new Standard.Factory(),
                new VAlarm.Factory(), new VAvailability.Factory(), new VEvent.Factory(),
                new VFreeBusy.Factory(), new VJournal.Factory(), new VTimeZone.Factory(),
                new VToDo.Factory(), new VVenue.Factory());
    }

    @Override
    public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Calendar calendar = new Calendar();
        assertTextValue(p, "vcalendar");
        // calendar properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                calendar.getProperties().add(parseProperty(p));
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        // calendar components..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                calendar.getComponents().add((CalendarComponent) parseComponent(p));
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return calendar;
    }

    private Component parseComponent(JsonParser p) throws IOException, URISyntaxException, ParseException {
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

    private Property parseProperty(JsonParser p) throws IOException, URISyntaxException, ParseException {
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
