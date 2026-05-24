package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.parameter.Feature;

import java.util.List;
import java.util.Optional;

/**
 * Builds the JSCalendar {@code virtualLocations} map from a component's
 * {@code CONFERENCE} properties.
 */
final class VirtualLocationBuilder {

    private VirtualLocationBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        List<Property> conferences = source.getProperties("CONFERENCE");
        if (conferences.isEmpty()) {
            return;
        }
        ObjectNode container = target.objectNode();
        int index = 1;
        for (Property conference : conferences) {
            ObjectNode node = target.objectNode();
            node.put("@type", "VirtualLocation");
            node.put("uri", conference.getValue());
            Optional<Parameter> label = conference.getParameter("LABEL");
            label.ifPresent(value -> node.put("name", value.getValue()));
            Optional<Feature> feature = conference.getParameter(Parameter.FEATURE);
            feature.ifPresent(value -> {
                ObjectNode features = target.objectNode();
                for (String token : value.getValue().split(",")) {
                    features.put(token.trim().toLowerCase(), true);
                }
                node.set("features", features);
            });
            container.set("conference-" + index, node);
            index++;
        }
        target.set("virtualLocations", container);
    }
}
