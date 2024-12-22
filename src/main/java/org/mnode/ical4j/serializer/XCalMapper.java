package org.mnode.ical4j.serializer;

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
import java.util.Arrays;
import java.util.List;

/**
 * Support for deserialization of {@link Calendar} objects encoded according to the XCal specification.
 */
public class XCalMapper extends StdDeserializer<Calendar> implements JsonMapper {

    private final ComponentMapper componentMapper;

    private final PropertyMapper propertyMapper;

    private final ParameterMapper parameterMapper;

    public XCalMapper(Class<?> vc) {
        super(vc);
        componentMapper = new ComponentMapperImpl(Arrays.asList(new Available.Factory(),
                new Daylight.Factory(), new Standard.Factory(),
                new VAlarm.Factory(), new VAvailability.Factory(), new VEvent.Factory(),
                new VFreeBusy.Factory(), new VJournal.Factory(), new VTimeZone.Factory(),
                new VToDo.Factory(), new VVenue.Factory()));
        propertyMapper = new PropertyMapperImpl(new DefaultPropertyFactorySupplier().get());
        parameterMapper = new ParameterMapperImpl(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var calendar = new Calendar();
        assertNextName(p, "vcalendar");
        assertNextToken(p, JsonToken.START_OBJECT);
        // calendar properties..
        assertNextName(p, "properties");
        assertNextToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            calendar.add(propertyMapper.map(p));
        }
        // calendar components..
        assertNextName(p, "components");
        // test for empty components..
        if (p.nextToken() != JsonToken.VALUE_STRING) {
            assertNextToken(p, JsonToken.START_OBJECT);
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                calendar.add((CalendarComponent) componentMapper.map(p));
            }
        }
        return calendar;
    }

    class ComponentMapperImpl implements ComponentMapper {

        private final List<ComponentFactory<?>> componentFactories;

        public ComponentMapperImpl(List<ComponentFactory<?>> componentFactories) {
            this.componentFactories = componentFactories;
        }

        @Override
        public Component map(JsonParser p) throws IOException {
            var componentName = p.currentName();
            assertNextToken(p, JsonToken.START_OBJECT);
            ComponentBuilder<?> componentBuilder = new ComponentBuilder<>(componentFactories);
            componentBuilder.name(componentName);
            // component properties..
            assertNextName(p, "properties");
            assertNextToken(p, JsonToken.START_OBJECT);
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                componentBuilder.property(propertyMapper.map(p));
            }
            // sub-components..
            assertNextName(p, "components");
            // test for empty sub-components..
            if (p.nextToken() != JsonToken.VALUE_STRING) {
                assertNextToken(p, JsonToken.START_OBJECT);
                while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                    componentBuilder.subComponent(map(p));
                }
            }
            return componentBuilder.build();
        }
    }

    class PropertyMapperImpl implements PropertyMapper {

        private final List<PropertyFactory<?>> propertyFactories;

        public PropertyMapperImpl(List<PropertyFactory<?>> propertyFactories) {
            this.propertyFactories = propertyFactories;
        }

        @Override
        public Property map(JsonParser p) throws IOException {
            var propertyName = p.currentName();
            assertNextToken(p, JsonToken.START_OBJECT);
            var propertyBuilder = new PropertyBuilder(propertyFactories);
            propertyBuilder.name(propertyName);
            // property params..
            assertNextName(p, "parameters");
            // test for empty parameters..
            if (p.nextToken() != JsonToken.VALUE_STRING) {
                assertCurrentToken(p, JsonToken.START_OBJECT);
                while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                    try {
                        propertyBuilder.parameter(parameterMapper.map(p));
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
            // propertyType
            var propertyType = p.nextFieldName();
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

            assertNextToken(p, JsonToken.END_OBJECT);

            return propertyBuilder.build();
        }
    }
}
