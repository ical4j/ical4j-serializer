package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.parameter.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Support for serialization of {@link Calendar} objects according to the JCal specification.
 */
public class JCalSerializer extends StdSerializer<Calendar> {

    public JCalSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildVCalendar(value));
    }

    private JsonNode buildVCalendar(Calendar calendar) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode vcalendar = mapper.createArrayNode();
        vcalendar.add("vcalendar");

        ArrayNode vcalprops = mapper.createArrayNode();
        for (Property p : calendar.getProperties()) {
            vcalprops.add(buildPropertyArray(p));
        }
        vcalendar.add(vcalprops);

        ArrayNode vcalcomponents = mapper.createArrayNode();
        for (Component c : calendar.getComponents()) {
            vcalcomponents.add(buildComponentArray(c));
        }
        vcalendar.add(vcalcomponents);

        return vcalendar;
    }

    private JsonNode buildComponentArray(Component component) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode cArray = mapper.createArrayNode();
        cArray.add(component.getName().toLowerCase());

        ArrayNode componentprops = mapper.createArrayNode();
        for (Property p : component.getProperties()) {
            componentprops.add(buildPropertyArray(p));
        }
        cArray.add(componentprops);

        ArrayNode subcomponents = mapper.createArrayNode();
        if (component instanceof ComponentContainer) {
            for (Component c : ((ComponentContainer<?>) component).getComponents()) {
                subcomponents.add(buildComponentArray(c));
            }
        }
        cArray.add(subcomponents);

        return cArray;
    }

    private JsonNode buildPropertyArray(Property property) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode pArray = mapper.createArrayNode();
        pArray.add(property.getName().toLowerCase());
        pArray.add(buildParamsObject(property.getParameters()));

        String propertyType = getPropertyType(property);
        pArray.add(propertyType);
        switch (propertyType) {
            case "date":
                pArray.add(JCalEncoder.DATE.encode(property.getValue()));
                break;
            case "date-time":
                pArray.add(JCalEncoder.DATE_TIME.encode(property.getValue()));
                break;
            case "time":
                pArray.add(JCalEncoder.TIME.encode(property.getValue()));
                break;
            case "utc-offset":
                pArray.add(JCalEncoder.UTCOFFSET.encode(property.getValue()));
                break;
            case "binary":
            case "duration":
            case "period":
            case "uri":
            default:
                pArray.add(property.getValue());
        }
        return pArray;
    }

    private String getPropertyType(Property property) {
        // handle property type overrides via VALUE param..
        Optional<Value> value = property.getParameter(Parameter.VALUE);
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
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        for (Parameter p : parameterList) {
            params.put(p.getName().toLowerCase(), p.getValue().toLowerCase());
        }
        return params;
    }
}
