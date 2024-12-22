package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyBuilder;
import net.fortuna.ical4j.model.PropertyFactory;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.ParameterName;
import net.fortuna.ical4j.vcard.VCardParameterFactorySupplier;
import net.fortuna.ical4j.vcard.VCardPropertyFactorySupplier;
import org.apache.commons.codec.DecoderException;
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

public class VCardMapper extends JsonDeserializer<Entity> implements JsonMapper {

    private final PropertyMapper propertyMapper;

    private final ParameterMapper parameterMapper;

    public VCardMapper(Class<?> vc) {
        propertyMapper = new PropertyMapperImpl(new VCardPropertyFactorySupplier().get());
        parameterMapper = new ParameterMapperImpl(new VCardParameterFactorySupplier().get());
    }

    @Override
    public Entity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var entity = new Entity();
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            var propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    entity.addAll(parsePropertyList(propertyName, p));
                } else {
                    entity.add(propertyMapper.map(p));
                }
            } catch (URISyntaxException | ParseException | DecoderException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return entity;
    }

    protected List<Property> parsePropertyList(String propertyName, JsonParser p) throws IOException, URISyntaxException, ParseException, DecoderException {
        List<Property> properties = new ArrayList<>();
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            properties.add(new PropertyMapperImpl(new VCardPropertyFactorySupplier().get(), propertyName).map(p));
        }
        return properties;
    }

    private boolean isParameter(String fieldName) {
        return Arrays.asList(ParameterName.PREF).contains(ParameterName.valueOf(fieldName));
    }

    class PropertyMapperImpl implements PropertyMapper {

        private final List<PropertyFactory<?>> propertyFactories;

        private final String propertyName;

        public PropertyMapperImpl(List<PropertyFactory<?>> propertyFactories) {
            this(propertyFactories, null);
        }

        public PropertyMapperImpl(List<PropertyFactory<?>> propertyFactories, String propertyName) {
            this.propertyFactories = propertyFactories;
            this.propertyName = propertyName;
        }

        @Override
        public Property map(JsonParser p) throws IOException {
            PropertyBuilder builder = new PropertyBuilder(propertyFactories);
            builder.name(propertyName != null ? propertyName : p.currentName());
            if (JsonToken.START_ARRAY.equals(p.currentToken())) {
                var b = new StringBuilder();
                while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
                    assertCurrentToken(p, JsonToken.VALUE_STRING);
                    b.append(p.getText());
                    b.append(',');
                }
                builder.value(b.toString());
            } else if (JsonToken.START_OBJECT.equals(p.currentToken())) {
                // parse parameters and value..
                while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                    assertCurrentToken(p, JsonToken.FIELD_NAME);
                    if (isParameter(p.currentName())) {
                        builder.parameter(parameterMapper.map(p));
                    } else {
                        assertNextScalarValue(p);
                        builder.value(p.getText());
                    }
                }
            } else {
                assertCurrentScalarValue(p);
                builder.value(p.getText());
            }

            return builder.build();
        }
    }
}
