package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyBuilder;
import net.fortuna.ical4j.model.PropertyFactory;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.VCardParameterFactorySupplier;
import net.fortuna.ical4j.vcard.VCardPropertyFactorySupplier;
import net.fortuna.ical4j.vcard.parameter.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Support for deserialization of {@link Entity} objects encoded according to the JCard specification.
 */
public class JCardMapper extends StdDeserializer<Entity> implements JsonMapper {

    private final PropertyMapper propertyMapper;

    private final ParameterMapper parameterMapper;

    public JCardMapper(Class<?> vc) {
        super(vc);
        propertyMapper = new PropertyMapperImpl(new VCardPropertyFactorySupplier().get());
        parameterMapper = new ParameterMapperImpl(new VCardParameterFactorySupplier().get());
    }

    @Override
    public Entity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        var entity = new Entity();
        assertTextValue(p, "vcard");
        // card properties..
        assertNextToken(p, JsonToken.START_ARRAY);
        while (!JsonToken.END_ARRAY.equals(p.nextToken())) {
            entity.add(propertyMapper.map(p));
        }
        return entity;
    }

    class PropertyMapperImpl implements PropertyMapper {

        private final List<PropertyFactory<?>> propertyFactories;

        public PropertyMapperImpl(List<PropertyFactory<?>> propertyFactories) {
            this.propertyFactories = propertyFactories;
        }

        @Override
        public Property map(JsonParser p) throws IOException {
            assertCurrentToken(p, JsonToken.START_ARRAY);
            PropertyBuilder builder = new PropertyBuilder(propertyFactories);
            builder.name(p.nextTextValue());
            // property params..
            assertNextToken(p, JsonToken.START_OBJECT);
            List<Parameter> params = new ArrayList<>();
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                builder.parameter(parameterMapper.map(p));
            }

            // propertyType
            var propertyType = p.nextTextValue();
            switch (propertyType) {
                case "binary":
                    builder.parameter(Value.BINARY);
                case "duration":
                    builder.parameter(Value.DURATION);
                case "date":
                    builder.parameter(Value.DATE);
                case "date-time":
                    builder.parameter(Value.DATE_TIME);
                case "uri":
                    builder.parameter(Value.URI);
            }

            builder.value(p.nextTextValue());
            assertNextToken(p, JsonToken.END_ARRAY);
            return builder.build();
        }
    }
}
