package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.vcard.ParameterFactoryRegistry;
import net.fortuna.ical4j.vcard.PropertyFactoryRegistry;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.parameter.Value;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Support for deserialization of {@link VCard} objects encoded according to the JCard specification.
 */
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
        var card = new VCard();
        assertTextValue(p, "vcard");
        // card properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            try {
                card.add(parseProperty(p));
            } catch (URISyntaxException | ParseException | DecoderException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return card;
    }

    private Property parseProperty(JsonParser p) throws IOException, URISyntaxException, ParseException, DecoderException {
        assertCurrentToken(p, JsonToken.START_ARRAY);
        var propName = p.nextTextValue();
        // property params..
        assertNextToken(p, JsonToken.START_OBJECT);
        List<Parameter> params = new ArrayList<>();
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            var parameter = parameterFactoryRegistry.getFactory(p.currentName()).createParameter(p.getText());
            params.add(parameter);
        }

        // propertyType
        var propertyType = p.nextTextValue();
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

        var value = p.nextTextValue();
        assertNextToken(p, JsonToken.END_ARRAY);

        return propertyFactoryRegistry.getFactory(propName).createProperty(new ParameterList(params), value);
    }
}
