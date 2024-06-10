package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.vcard.ParameterName;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.parameter.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Support for serialization of {@link VCard} objects according to the XCard specification.
 */
@JsonRootName(value = "vcards")
public class XCardSerializer extends StdSerializer<VCard> {

    private ObjectMapper objectMapper;

    public XCardSerializer(Class<VCard> t) {
        super(t);
        this.objectMapper = new XmlMapper(); //.builder().enable(SerializationFeature.WRAP_ROOT_VALUE)
    }

    @Override
    public void serialize(VCard value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildVCard(value));
    }

    private JsonNode buildVCard(VCard card) {
        ObjectNode root = objectMapper.createObjectNode();
        ObjectNode vcard = root.putObject("vcard");

        ObjectNode cardprops = vcard.putObject("properties");
        for (Property p : card.getProperties()) {
            cardprops.putIfAbsent(p.getName().toLowerCase(), buildPropertyNode(p));
        }
        return root;
    }

    private JsonNode buildPropertyNode(Property property) {
        ObjectNode pArray = objectMapper.createObjectNode();
        pArray.putIfAbsent("parameters", buildParamsObject(property.getParameters()));

        String propertyType = getPropertyType(property);
        switch (propertyType) {
            case "date":
                pArray.put(propertyType, JCalEncoder.DATE.encode(property.getValue()));
                break;
            case "date-time":
                pArray.put(propertyType, JCalEncoder.DATE_TIME.encode(property.getValue()));
                break;
            case "time":
                pArray.put(propertyType, JCalEncoder.TIME.encode(property.getValue()));
                break;
            case "utc-offset":
                pArray.put(propertyType, JCalEncoder.UTCOFFSET.encode(property.getValue()));
                break;
            case "binary":
            case "duration":
            case "period":
            case "uri":
            default:
                pArray.put(propertyType, property.getValue());
        }
        return pArray;
    }

    private String getPropertyType(Property property) {
        // handle property type overrides via VALUE param..
        Optional<Value> value = property.getParameter(ParameterName.VALUE);
        if (value.isPresent()) {
            return value.get().getValue().toLowerCase();
        }

        switch (property.getName()) {
            case "CALSCALE":
            case "METHOD":
            case "PRODID":
            case "VERSION":
            case "CATEGORIES":
            case "CLASS":
            case "COMMENT":
            case "DESCRIPTION":
            case "LOCATION":
            case "RESOURCES":
            case "STATUS":
            case "SUMMARY":
            case "TRANSP":
            case "TZID":
            case "TZNAME":
            case "CONTACT":
            case "RELATED-TO":
            case "UID":
            case "ACTION":
            case "REQUEST-STATUS":
            case "NAME":
                return "text";
            case "GEO":
                return "float";
            case "PERCENT-COMPLETE":
            case "PRIORITY":
            case "REPEAT":
            case "SEQUENCE":
                return "integer";
            case "COMPLETED":
            case "DTEND":
            case "DUE":
            case "DTSTAMP":
            case "DTSTART":
            case "DURATION":
            case "RECURRENCE-ID":
            case "EXDATE":
            case "RDATE":
            case "TRIGGER":
            case "CREATED":
            case "LAST-MODIFIED":
                return "date-time";
            case "FREEBUSY":
                return "period";
            case "TZOFFSETFROM":
            case "TZOFFSETTO":
                return "utc-offset";
            case "TZURL":
            case "URL":
            case "ATTACH":
            case "IMAGE":
            case "SOURCE":
                return "uri";
            case "ATTENDEE":
            case "ORGANIZER":
                return "cal-address";
            case "RRULE":
                return "recur";
            default:
                return "unknown";
        }
    }

    private JsonNode buildParamsObject(List<Parameter> parameterList) {
        ObjectNode params = objectMapper.createObjectNode();
        for (Parameter p : parameterList) {
            params.put(p.getName().toLowerCase(), p.getValue().toLowerCase());
        }
        return params;
    }
}
