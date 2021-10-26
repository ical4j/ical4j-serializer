package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;

public class SchemaOrganizationBuilder extends AbstractSchemaBuilder<VCard> {

    public SchemaOrganizationBuilder() {
        super("Organization");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("@id", node, component.getProperty(Property.Id.UID));
        putIfNotNull("name", node, component.getProperty(Property.Id.FN));
        putIfNotNull("email", node, component.getProperty(Property.Id.EMAIL));
        putIfNotNull("image", node, component.getProperty(Property.Id.PHOTO));
        putIfNotNull("logo", node, component.getProperty(Property.Id.LOGO));
        putIfNotNull("telephone", node, component.getProperty(Property.Id.TEL));
        putIfNotNull("url", node, component.getProperty(Property.Id.URL));
        setObject("address", node, component.getProperty(Property.Id.ADR));
        setObject("member", node, component.getProperty(Property.Id.MEMBER));
        return node;
    }
}
