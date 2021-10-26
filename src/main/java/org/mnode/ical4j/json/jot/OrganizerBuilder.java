package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.Organizer;

public class OrganizerBuilder extends AbstractJotBuilder<Organizer> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("cal-address", node, component.getValue());
        component.getParameters().forEach(parameter -> {
            node.put(parameter.getName().toLowerCase(), parameter.getValue());
        });
        return node;
    }
}
