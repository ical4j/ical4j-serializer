package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VToDo;

public class SchemaActionSerializer extends AbstractSchemaCalendarSerializer<VToDo> {

    public SchemaActionSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VToDo component) {
        AbstractSchemaBuilder<VToDo> builder = new SchemaActionBuilder().component(component);
        return builder.build();
    }
}
