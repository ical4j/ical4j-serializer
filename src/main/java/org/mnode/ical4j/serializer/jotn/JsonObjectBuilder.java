package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.parameter.Value;
import org.mnode.ical4j.serializer.JsonBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

/**
 * Provides support for building JSON nodes from a list of properties.
 */
public class JsonObjectBuilder implements JsonBuilder {

    private final PropertyContainer propertyContainer;

    private final List<String> propertyNames;

    public JsonObjectBuilder(PropertyContainer propertyContainer, List<String> propertyNames) {
        this.propertyContainer = propertyContainer;
        this.propertyNames = propertyNames;
    }

    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();

        Map<String, List<Property>> propertyMap = propertyContainer.getProperties().stream()
                .filter(p -> propertyNames.contains(p.getName()))
                .collect(groupingBy(Property::getName));

        //xxx: add single properties as string node
        // add multiple properties as array node
        for (String propName : propertyMap.keySet()) {
            List<Property> props = propertyMap.get(propName);
            if (props.size() == 1) {
                putProperty(node, props.get(0));
            } else {
                ArrayNode arrayNode = node.putArray(propName.toLowerCase());
                props.forEach(p -> putProperty(arrayNode, p));
            }
        }
        return node;
    }

    private ObjectNode putProperty(ObjectNode node, Property prop) {
        if (prop.getParameters().isEmpty()) {
            node.put(prop.getName().toLowerCase(), prop.getValue());
        } else {
            ObjectNode pobj = node.putObject(prop.getName().toLowerCase());
            prop.getParameters().forEach(p -> pobj.put(p.getName().toLowerCase(), p.getValue()));
            pobj.put(valueFieldName(prop), encodeValue(prop));
        }
        return node;
    }

    private ArrayNode putProperty(ArrayNode node, Property prop) {
        if (prop.getParameters().isEmpty()) {
            node.add(prop.getValue());
        } else {
            ObjectNode pobj = node.addObject();
            pobj.put(valueFieldName(prop), encodeValue(prop));
        }
        return node;
    }

    private String valueFieldName(Property prop) {
        switch (prop.getName()) {
            case "ORGANIZER":
                return "cal-address";
            default:
                Optional<Value> value = prop.getParameter("VALUE");
                return value.map(value1 -> value1.getValue().toLowerCase()).orElse("value");
        }
    }
}
