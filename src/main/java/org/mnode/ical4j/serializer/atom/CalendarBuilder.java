package org.mnode.ical4j.serializer.atom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Source;

public class CalendarBuilder extends AbstractFeedBuilder<Calendar> {

    public CalendarBuilder() {
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        component.getProperties().forEach(property -> {
            switch (property.getName()) {
                // skip properties that have no meaning in JSON Feed..
                case Property.PRODID:
                case Property.VERSION:
                    break;
                case Property.NAME:
                    node.put("title", property.getValue());
                    break;
                case Property.URL:
                    node.put("home_page_url", property.getValue());
                    break;
                case Source.PROPERTY_NAME:
                    node.put("feed_url", property.getValue());
                    break;
                default:
//                    node.put(property.getName().toLowerCase(), property.getValue());
            }
        });
        component.getComponents().forEach(component -> {
            ((ArrayNode) node.get("items")).add(
                    new ComponentBuilder<>(mapper.createObjectNode()).component(component).build());
        });
        return node;
    }
}
