package org.mnode.ical4j.serializer.jotn.property;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.property.Organizer;
import org.mnode.ical4j.serializer.jotn.AbstractJsonBuilder;

public class OrganizerJsonBuilder extends AbstractJsonBuilder<Organizer> {

    @Override
    public JsonNode build() {
        var node = createObjectNode();
        putIfNotNull("cal-address", node, component.getValue());
        component.getParameters().forEach(parameter -> {
            node.put(parameter.getName().toLowerCase(), parameter.getValue());
        });
        return node;
    }
}
