package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.property.Address;

public class PostalAddressNodeBuilder extends AbstractNodeBuilder<VCard> {

    public PostalAddressNodeBuilder() {
        super("PostalAddress");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        Address address = component.getRequiredProperty(PropertyName.ADR.toString());
        putIfNotNull("addressLocality", node, address.getLocality());
        putIfNotNull("addressRegion", node, address.getRegion());
        putIfNotNull("postalCode", node, address.getPostcode());
        putIfNotNull("streetAddress", node, address.getStreet());
        return node;
    }
}
