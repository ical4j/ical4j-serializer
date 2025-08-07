package org.mnode.ical4j.serializer.jotn.property;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.property.Organizer;
import org.mnode.ical4j.serializer.jotn.AbstractJsonBuilder;

/**
 * Converts iCal4j {@link Organizer} objects to JSON format.
 * <p>
 * This class extends {@link AbstractJsonBuilder} to provide a custom implementation for building
 * JSON representations of the Organizer property, including its parameters.
 */
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
