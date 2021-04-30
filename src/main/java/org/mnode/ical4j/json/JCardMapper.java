package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.vcard.*;
import net.fortuna.ical4j.vcard.parameter.Value;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class JCardMapper extends StdDeserializer<VCard> implements JsonMapper {

    private final ParameterFactoryRegistry parameterFactoryRegistry;

    private final PropertyFactoryRegistry propertyFactoryRegistry;

    public JCardMapper(Class<?> vc) {
        super(vc);
        parameterFactoryRegistry = new ParameterFactoryRegistry();
        propertyFactoryRegistry = new PropertyFactoryRegistry();
    }

    @Override
    public VCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        VCard card = new VCard();
        assertTextValue(p, "vcard");
        // card properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                card.getProperties().add(parseProperty(p));
            } catch (URISyntaxException | ParseException | DecoderException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return card;
    }

    private Property parseProperty(JsonParser p) throws IOException, URISyntaxException, ParseException, DecoderException {
        assertCurrentToken(p, JsonToken.START_ARRAY);
        String propName = p.nextTextValue();
        // property params..
        assertNextToken(p, JsonToken.START_OBJECT);
        List<net.fortuna.ical4j.vcard.Parameter> params = new ArrayList<>();
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            Parameter parameter = parameterFactoryRegistry.getFactory(p.currentName()).createParameter(
                    p.currentName(), p.getCurrentValue().toString());
            params.add(parameter);
        }

        // propertyType
        String propertyType = p.nextTextValue();
        switch (propertyType) {
            case "binary":
                params.add(Value.BINARY);
            case "duration":
                params.add(Value.DURATION);
            case "date":
                params.add(Value.DATE);
            case "date-time":
                params.add(Value.DATE_TIME);
            case "uri":
                params.add(Value.URI);
        }

        String value = p.nextTextValue();
        assertNextToken(p, JsonToken.END_ARRAY);

        return propertyFactoryRegistry.getFactory(propName).createProperty(params, value);
    }
}
