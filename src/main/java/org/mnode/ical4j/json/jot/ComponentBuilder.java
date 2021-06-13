package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Component;

public class ComponentBuilder<T extends Component> extends AbstractJotBuilder<T> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
//        putIfNotNull("uid", node, component.getProperty(Property.UID));
        component.getProperties().forEach(property -> {
            node.put(property.getName().toLowerCase(), property.getValue());
        });
        return node;
    }
}
