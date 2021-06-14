package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.*;
import org.mnode.ical4j.json.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        PropertyBuilder propertyBuilder = new PropertyBuilder().factories(propertyFactories);
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
        return new ParameterBuilder().factories(parameterFactories)
                .name(p.currentName()).value(p.getText()).build();
    }

    private boolean isParameter(String fieldName) {
        return Arrays.asList(Parameter.ABBREV, Parameter.CN, Parameter.ALTREP, Parameter.CUTYPE,
                Parameter.DIR, Parameter.DELEGATED_FROM, Parameter.DELEGATED_TO, Parameter.DISPLAY,
                Parameter.EMAIL, Parameter.ENCODING, Parameter.FBTYPE, Parameter.FEATURE,
                Parameter.FMTTYPE, Parameter.LABEL, Parameter.LANGUAGE, Parameter.MEMBER,
                Parameter.PARTSTAT, Parameter.RANGE, Parameter.RELATED, Parameter.RELTYPE,
                Parameter.ROLE, Parameter.RSVP, Parameter.SCHEDULE_AGENT, Parameter.SCHEDULE_STATUS,
                Parameter.SENT_BY, Parameter.TYPE, Parameter.TZID, Parameter.VALUE, Parameter.VVENUE).contains(fieldName.toUpperCase());
    }
}
