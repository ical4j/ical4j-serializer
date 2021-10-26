package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;

public class CalendarBuilder extends AbstractJotBuilder<Calendar> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
//        putIfNotNull("uid", node, component.getProperty(Property.UID));
        component.getProperties().forEach(property -> {
            switch (property.getName()) {
                // skip properties that have no meaning in jot..
                case Property.PRODID:
                case Property.VERSION:
                    break;
                default:
                    node.put(property.getName().toLowerCase(), property.getValue());
            }
        });
        return node;
    }
}
