package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;

/**
 * Support for serialization of objects to JSON encoded representations.
 */
public interface JsonBuilder {

    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, Property property) {
        if (property != null) {
            node.put(propertyName, property.getValue());
        }
        return node;
    }

//    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, DateProperty property) {
//        if (property != null) {
//            node.put(propertyName, property.getValue());
//        }
//        return node;
//    }

    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, net.fortuna.ical4j.vcard.Property property) {
        if (property != null) {
            node.put(propertyName, property.getValue());
        }
        return node;
    }

    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, String property) {
        if (property != null) {
            node.put(propertyName, property);
        }
        return node;
    }
}
