package org.mnode.ical4j.serializer.model.property;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.Organizer;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;

public class OrganizerJsonBuilder extends AbstractJsonBuilder<Organizer> {

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
