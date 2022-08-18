package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.parameter.Value;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Support for serialization of {@link Calendar} objects according to the XCal specification.
 */
@JsonRootName(value = "icalendar")
public class XCalSerializer extends StdSerializer<Calendar> {

    private ObjectMapper objectMapper;

    public XCalSerializer(Class<Calendar> t) {
        super(t);
        this.objectMapper = new XmlMapper(); //.builder().enable(SerializationFeature.WRAP_ROOT_VALUE)
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildVCalendar(value));
    }

    private JsonNode buildVCalendar(Calendar calendar) {
        ObjectNode icalendar = objectMapper.createObjectNode();
        ObjectNode vcalendar = icalendar.putObject("vcalendar");

        ObjectNode vcalprops = vcalendar.putObject("properties");
        for (Property p : calendar.getProperties()) {
            vcalprops.putIfAbsent(p.getName().toLowerCase(), buildPropertyNode(p));
        }

        ObjectNode vcalcomponents = vcalendar.putObject("components");
        for (Component c : calendar.getComponents()) {
            vcalcomponents.putIfAbsent(c.getName().toLowerCase(), buildComponentArray(c));
        }
        return icalendar;
    }

    private JsonNode buildComponentArray(Component component) {
        ObjectNode cArray = objectMapper.createObjectNode();

        ObjectNode componentprops = cArray.putObject("properties");
        for (Property p : component.getProperties()) {
            componentprops.putIfAbsent(p.getName().toLowerCase(), buildPropertyNode(p));
        }

        ObjectNode subcomponents = cArray.putObject("components");
        if (component instanceof VEvent) {
            for (Component c : ((VEvent) component).getAlarms()) {
                subcomponents.putIfAbsent(c.getName().toLowerCase(), buildComponentArray(c));
            }
        } else if (component instanceof VToDo) {
            for (Component c : ((VToDo) component).getAlarms()) {
                subcomponents.putIfAbsent(c.getName().toLowerCase(), buildComponentArray(c));
            }
        } else if (component instanceof VTimeZone) {
            for (Component c : ((VTimeZone) component).getObservances()) {
                subcomponents.putIfAbsent(c.getName().toLowerCase(), buildComponentArray(c));
            }
        }
        return cArray;
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
        ObjectNode params = objectMapper.createObjectNode();
        for (Parameter p : parameterList) {
            params.put(p.getName().toLowerCase(), p.getValue().toLowerCase());
        }
        return params;
    }
}
