package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VJournal;

public class SchemaCreativeWorkSerializer extends AbstractSchemaCalendarSerializer<VJournal> {

    public SchemaCreativeWorkSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VJournal component) {
        AbstractSchemaBuilder<VJournal> builder = new SchemaCreativeWorkBuilder().component(component);
        return builder.build();
    }
}
