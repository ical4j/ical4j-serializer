package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
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

/**
 * Support for deserialization of {@link Calendar} objects encoded according to the JCal specification.
 */
public class JCalMapper extends JsonDeserializer<Calendar> implements JsonMapper {

    private final List<ParameterFactory<?>> parameterFactories;

    private final List<PropertyFactory<?>> propertyFactories;

    private final List<ComponentFactory<?>> componentFactories;

    public JCalMapper(Class<?> vc) {
//        super(vc);
        parameterFactories = new DefaultParameterFactorySupplier().get();
        propertyFactories = new DefaultPropertyFactorySupplier().get();
        componentFactories = Arrays.asList(new Available.Factory(), new Daylight.Factory(), new Standard.Factory(),
                new VAlarm.Factory(), new VAvailability.Factory(), new VEvent.Factory(),
                new VFreeBusy.Factory(), new VJournal.Factory(), new VTimeZone.Factory(),
                new VToDo.Factory());
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
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        // calendar components..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                calendar.add((CalendarComponent) parseComponent(p));
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
                propertyBuilder.parameter(parseParameter(p, parameterFactories));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        // propertyType
        String propertyType = p.nextTextValue();
        switch (propertyType) {
            case "date":
                propertyBuilder.parameter(Value.DATE);
                propertyBuilder.value(JCalDecoder.DATE.decode(p.nextTextValue()));
                break;
            case "date-time":
                propertyBuilder.parameter(Value.DATE_TIME);
                propertyBuilder.value(JCalDecoder.DATE_TIME.decode(p.nextTextValue()));
                break;
            case "time":
                propertyBuilder.parameter(Value.TIME);
                propertyBuilder.value(JCalDecoder.TIME.decode(p.nextTextValue()));
                break;
            case "utc-offset":
                propertyBuilder.parameter(Value.UTC_OFFSET);
                propertyBuilder.value(JCalDecoder.UTCOFFSET.decode(p.nextTextValue()));
                break;
            case "binary":
                propertyBuilder.parameter(Value.BINARY);
            case "duration":
                propertyBuilder.parameter(Value.DURATION);
            case "period":
                propertyBuilder.parameter(Value.PERIOD);
            case "uri":
                propertyBuilder.parameter(Value.URI);
            default:
                propertyBuilder.value(p.nextTextValue());
        }

        assertNextToken(p, JsonToken.END_ARRAY);

        return propertyBuilder.build();
    }

}
