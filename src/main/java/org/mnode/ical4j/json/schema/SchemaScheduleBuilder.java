package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.component.VEvent;

public class SchemaScheduleBuilder extends AbstractSchemaBuilder<VEvent> {

    public SchemaScheduleBuilder() {
        super("Schedule");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        return node;
    }
}
