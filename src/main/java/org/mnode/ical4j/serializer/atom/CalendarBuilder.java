package org.mnode.ical4j.serializer.atom;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Source;

public class CalendarBuilder extends AbstractFeedBuilder<Calendar> {

    public CalendarBuilder() {
    }

    @Override
    public JsonNode build() {
        var node = mapper.createObjectNode();
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
                    node.putPOJO("link", new Link(property.getValue()));
                    break;
                case Source.PROPERTY_NAME:
                    node.putPOJO("link", new Link(property.getValue()).withRel("self"));
                    break;
                default:
//                    node.put(property.getName().toLowerCase(), property.getValue());
            }
        });
        component.getComponents().forEach(component -> {
            new ComponentBuilder<>(node.putObject("entry")).component(component).build();
        });
        return node;
    }
}
