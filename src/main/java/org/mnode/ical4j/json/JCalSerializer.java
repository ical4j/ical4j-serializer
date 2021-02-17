package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.component.VToDo;

import java.io.IOException;
import java.util.List;

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
        for (Property p : calendar.getProperties().getAll()) {
            vcalprops.add(buildPropertyArray(p));
        }
        vcalendar.add(vcalprops);

        ArrayNode vcalcomponents = mapper.createArrayNode();
        for (Component c : calendar.getComponents().getAll()) {
            vcalcomponents.add(buildComponentArray(c));
        }
        vcalendar.add(vcalcomponents);

        return vcalendar;
    }

    private JsonNode buildComponentArray(Component component) {
        ObjectMapper mapper = new ObjectMapper();

        ArrayNode cArray = mapper.createArrayNode();
        cArray.add(component.getName());

        ArrayNode componentprops = mapper.createArrayNode();
        for (Property p : component.getProperties().getAll()) {
            componentprops.add(buildPropertyArray(p));
        }
        cArray.add(componentprops);

        ArrayNode subcomponents = mapper.createArrayNode();
        if (component instanceof VEvent) {
            for (Component c : ((VEvent) component).getAlarms().getAll()) {
                subcomponents.add(buildComponentArray(c));
            }
        } else if (component instanceof VToDo) {
            for (Component c : ((VToDo) component).getAlarms().getAll()) {
                subcomponents.add(buildComponentArray(c));
            }
        } else if (component instanceof VTimeZone) {
            for (Component c : ((VTimeZone) component).getObservances().getAll()) {
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
        pArray.add(buildParamsObject(property.getParameters().getAll()));
        pArray.add(getPropertyType(property));
        pArray.add(property.getValue());

        return pArray;
    }

    private String getPropertyType(Property property) {
        switch (property.getName()) {
            case "DTSTAMP":
            case "DTSTART":
                return "date";
            case "PRODID":
            case "VERSION":
            case "UID":
                return "string";
        }
        throw new IllegalArgumentException("Unknown property type");
    }

    private JsonNode buildParamsObject(List<Parameter> parameterList) {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode params = mapper.createObjectNode();
        for (Parameter p : parameterList) {
            params.put(p.getName().toLowerCase(), p.getValue());
        }
        return params;
    }
}
