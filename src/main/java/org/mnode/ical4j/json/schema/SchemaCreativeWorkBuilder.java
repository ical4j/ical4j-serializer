package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VJournal;

public class SchemaCreativeWorkBuilder extends AbstractSchemaBuilder<VJournal> {

    public SchemaCreativeWorkBuilder() {
        super("CreativeWork");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        setProperty("@id", node, component.getProperty(Property.UID));
        setProperty("name", node, component.getProperty(Property.SUMMARY));
        setProperty("description", node, component.getProperty(Property.DESCRIPTION));
        setProperty("url", node, component.getProperty(Property.URL));
        return node;
    }
}
