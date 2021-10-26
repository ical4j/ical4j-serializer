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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JCalMapper extends StdDeserializer<Calendar> implements JsonMapper {

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
        ComponentBuilder<?> componentBuilder = new ComponentBuilder<>(componentFactories);
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
        PropertyBuilder propertyBuilder = new PropertyBuilder(propertyFactories);
        propertyBuilder.name(p.nextTextValue());
        // property params..
        assertNextToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            try {
                propertyBuilder.parameter(parseParameter(p));
            } catch (URISyntaxException | IOException e) {
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

        propertyBuilder.value(convert(p.nextTextValue(), propertyType));
        assertNextToken(p, JsonToken.END_ARRAY);

        return propertyBuilder.build();
    }

    private String convert(String value, String propertyType) {
        switch (propertyType) {
            case "date":
                return DateTimeFormatter.BASIC_ISO_DATE.format(
                        DateTimeFormatter.ISO_LOCAL_DATE.parse(value));
            case "date-time":
                return DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").format(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(value));
            case "binary":
            case "duration":
            case "period":
            case "uri":
            default:
                return value;
        }
    }

    private Parameter parseParameter(JsonParser p) throws IOException, URISyntaxException {
        return new ParameterBuilder(parameterFactories)
                .name(p.currentName()).value(p.nextTextValue()).build();
    }
}
