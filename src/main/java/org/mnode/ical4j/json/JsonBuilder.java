package org.mnode.ical4j.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;

public interface JsonBuilder {

    default ObjectNode putIfNotNull(String propertyName, ObjectNode node, Property property) {
        if (property != null) {
            node.put(propertyName, property.getValue());
        }
        return node;
    }

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
