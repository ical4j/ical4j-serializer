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
        putIfNotNull("@id", node, component.getProperty(Property.Id.UID));
        putIfNotNull("name", node, component.getProperty(Property.Id.FN));
        putIfNotNull("email", node, component.getProperty(Property.Id.EMAIL));
        putIfNotNull("image", node, component.getProperty(Property.Id.PHOTO));
        putIfNotNull("jobTitle", node, component.getProperty(Property.Id.TITLE));
        putIfNotNull("telephone", node, component.getProperty(Property.Id.TEL));
        putIfNotNull("url", node, component.getProperty(Property.Id.URL));
        setObject("address", node, component.getProperty(Property.Id.ADR));
        return node;
    }
}
