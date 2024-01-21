package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import net.fortuna.ical4j.model.*;
import org.mnode.ical4j.serializer.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.fortuna.ical4j.model.Parameter.*;

public interface JsonObjectMapper extends JsonMapper {

    List<ParameterFactory<?>> getParameterFactories();

    List<PropertyFactory<?>> getPropertyFactories();

    default <T extends PropertyContainer> T map(JsonParser p, T container) throws IOException {
        assertCurrentToken(p, JsonToken.START_OBJECT);

        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    container.addAll(parsePropertyList(p, propertyName));
                } else {
                    container.add(parseProperty(p, propertyName));
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
     * @param propertyName the name of a property type
     * @return a list of properties derived from traversing an array via the JSON parser
     * @throws IOException
     * @throws URISyntaxException
     * @throws ParseException
     */
    default List<Property> parsePropertyList(JsonParser p, String propertyName) throws IOException, URISyntaxException, ParseException {
        List<Property> properties = new ArrayList<>();
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            properties.add(parseProperty(p, propertyName));
        }
        return properties;
    }

    /**
     * Parse a single property value from a JSON parser.
     *
     * @param p            a JSON parser
     * @param propertyName name of a property type
     * @return a property dervied from traversing the JSON parser
     * @throws IOException
     * @throws URISyntaxException
     * @throws ParseException
     */
    default Property parseProperty(JsonParser p, String propertyName) throws IOException, URISyntaxException, ParseException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(getPropertyFactories());
        propertyBuilder.name(propertyName);
        if (JsonToken.START_ARRAY.equals(p.currentToken())) {
            StringBuilder b = new StringBuilder();
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
                    propertyBuilder.parameter(parseParameter(p, getParameterFactories()));
                } else {
                    assertNextScalarValue(p);
                    propertyBuilder.value(decodeValue(propertyName, p.getText()));
                }
            }
        } else {
            assertCurrentScalarValue(p);
            propertyBuilder.value(decodeValue(propertyName, p.getText()));
        }

        return propertyBuilder.build();
    }

    default boolean isParameter(String fieldName) {
        return Arrays.asList(ABBREV, CN, ALTREP, CUTYPE,
                DIR, DELEGATED_FROM, DELEGATED_TO, DISPLAY,
                EMAIL, ENCODING, FBTYPE, FEATURE,
                FMTTYPE, LABEL, LANGUAGE, MEMBER,
                PARTSTAT, RANGE, RELATED, RELTYPE,
                ROLE, RSVP, SCHEDULE_AGENT, SCHEDULE_STATUS,
                SENT_BY, TYPE, TZID, VALUE, VVENUE).contains(fieldName.toUpperCase()) ||
                fieldName.startsWith("x-");
    }

}
