package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;

public class SchemaPersonBuilder extends AbstractSchemaBuilder<VCard> {

    public SchemaPersonBuilder() {
        super("Person");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        setProperty("@id", node, component.getProperty(Property.Id.UID));
        setProperty("name", node, component.getProperty(Property.Id.FN));
        setProperty("email", node, component.getProperty(Property.Id.EMAIL));
        setProperty("image", node, component.getProperty(Property.Id.PHOTO));
        setProperty("jobTitle", node, component.getProperty(Property.Id.TITLE));
        setProperty("telephone", node, component.getProperty(Property.Id.TEL));
        setProperty("url", node, component.getProperty(Property.Id.URL));
        setObject("address", node, component.getProperty(Property.Id.ADR));
        return node;
    }
}
