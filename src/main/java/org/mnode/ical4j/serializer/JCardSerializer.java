package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.ParameterName;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.parameter.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Support for serialization of {@link Entity} objects according to the JCard specification.
 */
public class JCardSerializer extends StdSerializer<Entity> {

    public JCardSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildVCard(value));
    }

    private JsonNode buildVCard(Entity entity) {
        var mapper = new ObjectMapper();

        var vcard = mapper.createArrayNode();
        vcard.add("vcard");

        var vcardprops = mapper.createArrayNode();
        for (var p : entity.getProperties()) {
            vcardprops.add(buildPropertyArray(p));
        }
        vcard.add(vcardprops);

        return vcard;
    }

    private JsonNode buildPropertyArray(Property property) {
        var mapper = new ObjectMapper();

        var pArray = mapper.createArrayNode();
        pArray.add(property.getName().toLowerCase());
        pArray.add(buildParamsObject(property.getParameters()));
        pArray.add(getPropertyType(property));
        pArray.add(property.getValue());

        return pArray;
    }

    private String getPropertyType(Property property) {
        // handle property type overrides via VALUE param..
        Optional<Value> value = property.getParameter(ParameterName.VALUE.toString());
        if (value.isPresent()) {
            return value.get().getValue().toLowerCase();
        }

        switch (PropertyName.valueOf(property.getName())) {
            case KIND:
//            case XML:
            case FN:
            case N:
            case NICKNAME:
            case GENDER:
            case ADR:
            case TEL:
            case EMAIL:
            case TZ:
            case TITLE:
            case ROLE:
            case ORG:
            case CATEGORIES:
            case NOTE:
            case PRODID:
//            case CLIENTPIDMAP:
            case VERSION:
                return "text";
            case LANG:
                return "language-tag";
            case PHOTO:
            case GEO:
            case IMPP:
            case LOGO:
            case MEMBER:
            case RELATED:
            case SOUND:
            case UID:
            case CALURI:
            case CALADRURI:
            case URL:
            case KEY:
            case FBURL:
            case SOURCE:
                return "uri";
            case BDAY:
//            case ANNIVERSARY:
                return "date";
            case REV:
                return "timestamp";
        }
        throw new IllegalArgumentException("Unknown property type");
    }

    private JsonNode buildParamsObject(List<Parameter> parameterList) {
        var mapper = new ObjectMapper();

        var params = mapper.createObjectNode();
        for (var p : parameterList) {
            params.put(p.getName().toLowerCase(), p.getValue().toLowerCase());
        }
        return params;
    }
}
