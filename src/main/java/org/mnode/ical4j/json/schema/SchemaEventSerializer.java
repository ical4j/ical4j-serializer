package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VEvent;

public class SchemaEventSerializer extends AbstractSchemaCalendarSerializer<VEvent> {

    public SchemaEventSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VEvent component) {
        AbstractSchemaBuilder<VEvent> builder = new SchemaEventBuilder().component(component);
        return builder.build();
    }
}
