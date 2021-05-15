package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VAvailability;

public class SchemaServiceSerializer extends AbstractSchemaCalendarSerializer<VAvailability> {

    public SchemaServiceSerializer(Class<VAvailability> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VAvailability component) {
        AbstractSchemaBuilder<VAvailability> builder = new SchemaServiceBuilder().component(component);
        return builder.build();
    }
}
