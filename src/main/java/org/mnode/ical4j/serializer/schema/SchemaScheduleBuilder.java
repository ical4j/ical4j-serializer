package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.RRule;

public class SchemaScheduleBuilder extends AbstractSchemaBuilder<RRule> {

    public SchemaScheduleBuilder() {
        super("Schedule");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        return node;
    }
}
