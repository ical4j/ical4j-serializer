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
        putIfNotNull("addressLocality", node, component.getLocality());
        putIfNotNull("addressRegion", node, component.getRegion());
        putIfNotNull("postalCode", node, component.getPostcode());
        putIfNotNull("streetAddress", node, component.getStreet());
        return node;
    }
}
