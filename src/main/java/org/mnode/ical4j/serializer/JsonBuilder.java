package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.parameter.Value;

import java.util.Optional;

/**
 * Support for serialization of objects to JSON encoded representations.
 */
public interface JsonBuilder {

    default ObjectNode putIfNotAbsent(String propertyName, ObjectNode node, Optional<Property> property) {
        property.ifPresent(value -> node.put(propertyName, encodeValue(value)));
        return node;
    }

//    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, DateProperty property) {
//        if (property != null) {
//            node.put(propertyName, property.getValue());
//        }
//        return node;
//    }

    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, String property) {
        if (property != null) {
            node.put(propertyName, property);
        }
        return node;
    }

    default String encodeValue(Property property) {
        switch (property.getName().toLowerCase()) {
            case "trigger":
            case "created":
            case "last-modified":
            case "recurrence-id":
            case "dtstamp":
                return JCalEncoder.INSTANT.encode(property.getValue());
            case "dtstart":
            case "dtend":
            case "rdate":
            case "exdate":
            case "due":
                if (property.getParameters("VALUE").contains(Value.DATE)) {
                    return JCalEncoder.DATE.encode(property.getValue());
                } else {
                    return JCalEncoder.DATE_TIME.encode(property.getValue());
                }
            default: return property.getValue();
        }
    }
}
