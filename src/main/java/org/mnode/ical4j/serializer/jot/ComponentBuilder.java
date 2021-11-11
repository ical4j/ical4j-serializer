package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Organizer;

public class ComponentBuilder<T extends Component> extends AbstractJotBuilder<T> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
//        putIfNotNull("uid", node, component.getProperty(Property.UID));
        component.getProperties().forEach(property -> {
            switch (property.getName()) {
                case Property.ORGANIZER:
                    node.set(property.getName().toLowerCase(), new OrganizerBuilder()
                            .component((Organizer) property).build());
                    break;
                case Property.LOCATION:
                case Property.RESOURCES:
                case Property.ATTACH:
                case Property.RELATED_TO:
                case Property.ATTENDEE:
                case Property.TRIGGER:
                case Property.COMMENT:
                case Property.CONTACT:
                case Property.FREEBUSY:
                default:
                    node.put(property.getName().toLowerCase(), property.getValue());
            }
        });
        return node;
    }
}
