package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;

public class CalendarBuilder extends AbstractJotBuilder<Calendar> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("id", node, component.getProperty(Property.UID));
        return node;
    }
}
