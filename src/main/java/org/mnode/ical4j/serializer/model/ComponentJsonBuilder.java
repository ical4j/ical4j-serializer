package org.mnode.ical4j.serializer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Organizer;
import org.mnode.ical4j.serializer.model.property.OrganizerJsonBuilder;

public class ComponentJsonBuilder<T extends Component> extends AbstractJsonBuilder<T> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
//        putIfNotNull("uid", node, component.getProperty(Property.UID));
        component.getProperties().forEach(property -> {
            switch (property.getName()) {
                case Property.ORGANIZER:
                    node.set(property.getName().toLowerCase(), new OrganizerJsonBuilder()
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
