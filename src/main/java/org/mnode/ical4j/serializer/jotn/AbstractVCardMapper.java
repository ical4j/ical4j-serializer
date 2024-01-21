package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.vcard.ParameterFactoryRegistry;
import net.fortuna.ical4j.vcard.ParameterName;
import net.fortuna.ical4j.vcard.PropertyFactoryRegistry;
import org.apache.commons.codec.DecoderException;
import org.mnode.ical4j.serializer.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractVCardMapper<T> extends StdDeserializer<T> implements JsonMapper {

    private final PropertyFactoryRegistry propertyFactoryRegistry;

    private final ParameterFactoryRegistry parameterFactoryRegistry;

    public AbstractVCardMapper(Class<?> vc) {
        super(vc);
        propertyFactoryRegistry = new PropertyFactoryRegistry();
        parameterFactoryRegistry = new ParameterFactoryRegistry();
    }

    protected List<Property> parsePropertyList(String propertyName, JsonParser p) throws IOException, URISyntaxException, ParseException, DecoderException {
        List<Property> properties = new ArrayList<>();
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            properties.add(parseProperty(propertyName, p));
        }
        return properties;
    }

    protected Property parseProperty(String propertyName, JsonParser p) throws IOException, URISyntaxException, ParseException, DecoderException {
        String value = null;
        List<Parameter> parameters = new ArrayList<>();
        if (JsonToken.START_ARRAY.equals(p.currentToken())) {
            StringBuilder b = new StringBuilder();
            while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
                assertCurrentToken(p, JsonToken.VALUE_STRING);
                b.append(p.getText());
                b.append(',');
            }
            value = b.toString();
        } else if (JsonToken.START_OBJECT.equals(p.currentToken())) {
            // parse parameters and value..
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                assertCurrentToken(p, JsonToken.FIELD_NAME);
                if (isParameter(p.currentName())) {
                    parameters.add(parseParameter(p));
                } else {
                    assertNextScalarValue(p);
                    value = p.getText();
                }
            }
        } else {
            assertCurrentScalarValue(p);
            value = p.getText();
        }

        return propertyFactoryRegistry.getFactory(propertyName).createProperty(new ParameterList(parameters), value);
    }

    protected Parameter parseParameter(JsonParser p) throws IOException {
        assertNextScalarValue(p);
        return parameterFactoryRegistry.getFactory(p.currentName()).createParameter(p.getText());
    }

    private boolean isParameter(String fieldName) {
        return Arrays.asList(ParameterName.PREF).contains(ParameterName.valueOf(fieldName));
    }
}
