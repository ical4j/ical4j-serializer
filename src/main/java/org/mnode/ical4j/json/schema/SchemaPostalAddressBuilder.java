package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.property.Address;

public class SchemaPostalAddressBuilder extends AbstractSchemaBuilder<Address> {

    public SchemaPostalAddressBuilder() {
        super("PostalAddress");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        setProperty("addressLocality", node, component.getLocality());
        setProperty("addressRegion", node, component.getRegion());
        setProperty("postalCode", node, component.getPostcode());
        setProperty("streetAddress", node, component.getStreet());
        return node;
    }
}
