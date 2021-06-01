package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAvailability;

public class AvailabilityBuilder extends AbstractJotBuilder<VAvailability> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("uid", node, component.getProperty(Property.UID));
        return node;
    }
}
