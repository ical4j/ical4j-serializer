package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.*;
import org.mnode.ical4j.serializer.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.fortuna.ical4j.model.Parameter.*;

public abstract class AbstractJotCalMapper<T> extends StdDeserializer<T> implements JsonMapper {

    private final List<ParameterFactory<?>> parameterFactories;

    private final List<PropertyFactory<?>> propertyFactories;

    public AbstractJotCalMapper(Class<?> vc) {
        super(vc);
        parameterFactories = new DefaultParameterFactorySupplier().get();
        propertyFactories = new DefaultPropertyFactorySupplier().get();
    }

    protected List<Property> parsePropertyList(String propertyName, JsonParser p) throws IOException, URISyntaxException, ParseException {
        List<Property> properties = new ArrayList<>();
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            properties.add(parseProperty(propertyName, p));
        }
        return properties;
    }

    protected Property parseProperty(String propertyName, JsonParser p) throws IOException, URISyntaxException, ParseException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(propertyFactories);
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
                    propertyBuilder.parameter(parseParameter(p));
                } else {
                    assertNextScalarValue(p);
                    propertyBuilder.value(p.getText());
                }
            }
        } else {
            assertCurrentScalarValue(p);
            propertyBuilder.value(p.getText());
        }

        return propertyBuilder.build();
    }

    protected Parameter parseParameter(JsonParser p) throws IOException, URISyntaxException {
        assertNextScalarValue(p);
        return new ParameterBuilder(parameterFactories)
                .name(p.currentName()).value(p.getText()).build();
    }

    private boolean isParameter(String fieldName) {
        return Arrays.asList(ABBREV, CN, ALTREP, CUTYPE,
                DIR, DELEGATED_FROM, DELEGATED_TO, DISPLAY,
                EMAIL, ENCODING, FBTYPE, FEATURE,
                FMTTYPE, LABEL, LANGUAGE, MEMBER,
                PARTSTAT, RANGE, RELATED, RELTYPE,
                ROLE, RSVP, SCHEDULE_AGENT, SCHEDULE_STATUS,
                SENT_BY, TYPE, TZID, VALUE, VVENUE).contains(fieldName.toUpperCase());
    }
}
