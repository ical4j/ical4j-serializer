package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;

public class SchemaPlaceBuilder extends AbstractSchemaBuilder<VCard> {

    public SchemaPlaceBuilder() {
        super("Place");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("@id", node, component.getProperty(Property.Id.UID));
        putIfNotNull("name", node, component.getProperty(Property.Id.FN));
        putIfNotNull("image", node, component.getProperty(Property.Id.PHOTO));
        putIfNotNull("url", node, component.getProperty(Property.Id.URL));
        setObject("address", node, component.getProperty(Property.Id.ADR));
        return node;
    }
}
