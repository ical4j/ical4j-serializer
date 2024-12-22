package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyBuilder;
import net.fortuna.ical4j.model.PropertyFactory;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.vcard.Entity;

import java.io.IOException;
import java.util.List;

/**
 * Support for deserialization of {@link Entity} objects encoded according to the XCard specification.
 */
public class XCardMapper extends StdDeserializer<Entity> implements JsonMapper {

    private final PropertyMapper propertyMapper;

    private final ParameterMapper parameterMapper;

    public XCardMapper(Class<?> vc) {
        super(vc);
        propertyMapper = new PropertyMapperImpl(new DefaultPropertyFactorySupplier().get());
        parameterMapper = new ParameterMapperImpl(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public Entity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var card = new Entity();
        assertNextName(p, "vcard");
        assertNextToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            card.add(propertyMapper.map(p));
        }
        return card;
    }

    class PropertyMapperImpl implements PropertyMapper {

        private final List<PropertyFactory<?>> propertyFactories;

        public PropertyMapperImpl(List<PropertyFactory<?>> propertyFactories) {
            this.propertyFactories = propertyFactories;
        }

        @Override
        public Property map(JsonParser p) throws IOException {
            var builder = new PropertyBuilder(propertyFactories);
            builder.name(p.currentName());
            assertNextToken(p, JsonToken.START_OBJECT);
            // property params..
            assertNextName(p, "parameters");
            // test for empty parameters..
            if (p.nextToken() != JsonToken.VALUE_STRING) {
                assertCurrentToken(p, JsonToken.START_OBJECT);
                while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                    try {
                        builder.parameter(parameterMapper.map(p));
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            }
            // propertyType
            var propertyType = p.nextFieldName();
            switch (propertyType) {
                case "date":
                    builder.parameter(Value.DATE);
                    builder.value(JCalDecoder.DATE.decode(p.nextTextValue()));
                    break;
                case "date-time":
                    builder.parameter(Value.DATE_TIME);
                    builder.value(JCalDecoder.DATE_TIME.decode(p.nextTextValue()));
                    break;
                case "time":
                    builder.parameter(Value.TIME);
                    builder.value(JCalDecoder.TIME.decode(p.nextTextValue()));
                    break;
                case "utc-offset":
                    builder.parameter(Value.UTC_OFFSET);
                    builder.value(JCalDecoder.UTCOFFSET.decode(p.nextTextValue()));
                    break;
                case "binary":
                    builder.parameter(Value.BINARY);
                case "duration":
                    builder.parameter(Value.DURATION);
                case "period":
                    builder.parameter(Value.PERIOD);
                case "uri":
                    builder.parameter(Value.URI);
                default:
                    builder.value(p.nextTextValue());
            }
            assertNextToken(p, JsonToken.END_OBJECT);
            return builder.build();
        }
    }
}
