package org.mnode.ical4j.serializer.atom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

public class ComponentBuilder<T extends Component> extends AbstractFeedBuilder<T> {

    private final ObjectNode node;

    public ComponentBuilder(ObjectNode objectNode) {
        this.node = objectNode;
    }

    @Override
    public JsonNode build() {
        component.getProperties().forEach(property -> {
            switch (property.getName()) {
                // skip properties that have no meaning in JSON Feed..
                case Property.UID:
                    node.put("id", property.getValue());
                    break;
                case Property.SUMMARY:
                    node.put("summary", property.getValue());
                    break;
                case Property.URL:
                    node.put("url", property.getValue());
                    break;
                case Property.DESCRIPTION:
                    node.put("content_text", property.getValue());
                    break;
                case Property.STYLED_DESCRIPTION:
                    node.put("content_html", property.getValue());
                    break;
                case Property.DTSTAMP:
                    node.put("date_published", property.getValue());
                    break;
                case Property.LAST_MODIFIED:
                    node.put("date_modified", property.getValue());
                    break;
                default:
//                    node.put(property.getName().toLowerCase(), property.getValue());
            }
        });
        return node;
    }
}
