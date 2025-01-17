package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.*;
import org.mnode.ical4j.serializer.JsonMapper;
import org.mnode.ical4j.serializer.ParameterMapper;
import org.mnode.ical4j.serializer.ParameterMapperImpl;
import org.mnode.ical4j.serializer.PropertyMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static net.fortuna.ical4j.model.Parameter.*;

/**
 * Map iCalendar components and calendar objects from JSON.
 * @param <T>
 */
public class ContentMapper<T extends PropertyContainer> extends JsonDeserializer<T> implements JsonMapper {

    private final Supplier<T> supplier;

    private final PropertyMapper propertyMapper;

    private final ParameterMapper parameterMapper;

    public ContentMapper(Supplier<T> supplier) {
        this.supplier = supplier;
        propertyMapper = new PropertyMapperImpl(new DefaultPropertyFactorySupplier().get());
        parameterMapper = new ParameterMapperImpl(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        assertCurrentToken(p, JsonToken.START_OBJECT);
        T container = supplier.get();
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    container.addAll(parsePropertyList(p, propertyName));
                } else {
                    container.add(propertyMapper.map(p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return container;
    }


    /**
     * Parse an array of property values as the specified property type.
     *
     * @param p            a JSON parser
     * @return a list of properties derived from traversing an array via the JSON parser
     * @throws IOException
     * @throws URISyntaxException
     * @throws ParseException
     */
    private List<Property> parsePropertyList(JsonParser p, String propertyName) throws IOException, URISyntaxException, ParseException {
        List<Property> properties = new ArrayList<>();
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            properties.add(new PropertyMapperImpl(new DefaultPropertyFactorySupplier().get(), propertyName).map(p));
        }
        return properties;
    }

    class PropertyMapperImpl implements PropertyMapper {

        private final List<PropertyFactory<? extends Property>> propertyFactories;

        private final String propertyName;

        public PropertyMapperImpl(List<PropertyFactory<? extends Property>> propertyFactories) {
            this(propertyFactories, null);
        }

        public PropertyMapperImpl(List<PropertyFactory<? extends Property>> propertyFactories,
                                  String propertyName) {
            this.propertyFactories = propertyFactories;
            this.propertyName = propertyName;
        }

        @Override
        public Property map(JsonParser p) throws IOException {
            var propertyBuilder = new PropertyBuilder(propertyFactories);
//            propertyBuilder.name(propertyName != null ? propertyName.toUpperCase() : p.currentName().toUpperCase());
            String propName = parsePropertyName(p, propertyBuilder);
            if (JsonToken.START_ARRAY.equals(p.currentToken())) {
                var b = new StringBuilder();
                while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
                    assertCurrentToken(p, JsonToken.VALUE_STRING);
                    b.append(p.getText());
                    b.append(',');
                }
                propertyBuilder.value(b.toString());
            } else if (JsonToken.START_OBJECT.equals(p.currentToken())) {
                // parse parameters and value..
                while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                    assertCurrentToken(p, JsonToken.FIELD_NAME);
                    if (isParameter(p.currentName())) {
                        propertyBuilder.parameter(parameterMapper.map(p));
                    } else {
                        assertNextScalarValue(p);
                        propertyBuilder.value(decodeValue(propName, p.getText()));
                    }
                }
            } else {
                assertCurrentScalarValue(p);
                if (!p.getText().isBlank()) {
                    propertyBuilder.value(decodeValue(propName, p.getText()));
                }
            }

            return propertyBuilder.build();
        }

        private String parsePropertyName(JsonParser parser, PropertyBuilder builder) throws IOException {
            // if name already set return..
            if (propertyName != null) {
                builder.name(propertyName.toUpperCase());
                return propertyName;
            } else {
                String[] name = parser.currentName().split("[\\[\\]\\s]+");
                builder.name(name[0].toUpperCase());
                if (name.length > 1) {
                    // parse inline params..
                    for (int i = 1; i < name.length; i++) {
                        String[] params = name[i].split("[,\\s]+");
                        for (String param : params) {
                            String[] p = param.split(":");
                            builder.parameter(new ParameterBuilder().name(p[0]).value(p[1]).build());
                        }
                    }
                }
                return name[0];
            }
        }

        private boolean isParameter(String fieldName) {
            return Arrays.asList(ABBREV, CN, ALTREP, CUTYPE,
                    DIR, DELEGATED_FROM, DELEGATED_TO, DISPLAY,
                    EMAIL, ENCODING, FBTYPE, FEATURE,
                    FMTTYPE, LABEL, LANGUAGE, MEMBER,
                    PARTSTAT, RANGE, RELATED, RELTYPE,
                    ROLE, RSVP, SCHEDULE_AGENT, SCHEDULE_STATUS,
                    SENT_BY, TYPE, TZID, VALUE, VVENUE).contains(fieldName.toUpperCase()) ||
                    fieldName.toUpperCase().startsWith("X-");
        }

    }
}
