package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;

import java.util.Optional;

/**
 * Support for serialization of objects to JSON encoded representations.
 */
public interface JsonBuilder {

    default ObjectNode putIfNotAbsent(String propertyName, ObjectNode node, Optional<Property> property) {
        property.ifPresent(value -> node.put(propertyName, value.getValue()));
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
}
