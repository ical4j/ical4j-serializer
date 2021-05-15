package org.mnode.ical4j.json.schema;

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
        setProperty("@id", node, component.getProperty(Property.UID));
        return node;
    }
}
