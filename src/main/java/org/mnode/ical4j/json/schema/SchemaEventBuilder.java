package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

public class SchemaEventBuilder extends AbstractSchemaBuilder<VEvent> {

    public SchemaEventBuilder() {
        super("Event");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("@id", node, component.getProperty(Property.UID));
        putIfNotNull("name", node, component.getProperty(Property.SUMMARY));
        putIfNotNull("description", node, component.getProperty(Property.DESCRIPTION));
        putIfNotNull("url", node, component.getProperty(Property.URL));
        putIfNotNull("startDate", node, component.getProperty(Property.DTSTART));
        putIfNotNull("endDate", node, component.getProperty(Property.DTEND));
        putIfNotNull("location", node, component.getProperty(Property.LOCATION));
        return node;
    }
}
