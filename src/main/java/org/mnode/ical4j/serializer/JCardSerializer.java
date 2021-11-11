package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.vcard.Parameter;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.parameter.Value;

import java.io.IOException;
import java.util.List;

public class JCardSerializer extends StdSerializer<VCard> {

    public JCardSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildVCard(value));
    }

    private JsonNode buildVCard(VCard card) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode vcard = mapper.createArrayNode();
        vcard.add("vcard");

        ArrayNode vcardprops = mapper.createArrayNode();
        for (Property p : card.getProperties()) {
            vcardprops.add(buildPropertyArray(p));
        }
        vcard.add(vcardprops);

        return vcard;
    }

    private JsonNode buildPropertyArray(Property property) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode pArray = mapper.createArrayNode();
        pArray.add(property.getId().toString().toLowerCase());
        pArray.add(buildParamsObject(property.getParameters()));
        pArray.add(getPropertyType(property));
        pArray.add(property.getValue());

        return pArray;
    }

    private String getPropertyType(Property property) {
        // handle property type overrides via VALUE param..
        Value value = property.getParameter(Parameter.Id.VALUE);
        if (value != null) {
            return value.getValue().toLowerCase();
        }

        switch (property.getId()) {
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
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        for (Parameter p : parameterList) {
            params.put(p.getId().toString().toLowerCase(), p.getValue().toLowerCase());
        }
        return params;
    }
}
