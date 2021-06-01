package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAlarm;

public class AlarmBuilder extends AbstractJotBuilder<VAlarm> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("uid", node, component.getProperty(Property.UID));
        putIfNotNull("action", node, component.getProperty(Property.ACTION));
        putIfNotNull("trigger", node, component.getProperty(Property.TRIGGER));
        return node;
    }
}
