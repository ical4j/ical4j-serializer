package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;

public class SchemaActionBuilder extends AbstractSchemaBuilder<VToDo> {

    public SchemaActionBuilder() {
        super("Action");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("@id", node, component.getProperty(Property.UID));
        putIfNotNull("name", node, component.getProperty(Property.SUMMARY));
        putIfNotNull("description", node, component.getProperty(Property.DESCRIPTION));
        putIfNotNull("url", node, component.getProperty(Property.URL));
        putIfNotNull("startTime", node, component.getProperty(Property.DTSTART));
        putIfNotNull("endTime", node, component.getProperty(Property.DUE));
        putIfNotNull("actionStatus", node, component.getProperty(Property.STATUS));
        return node;
    }
}
