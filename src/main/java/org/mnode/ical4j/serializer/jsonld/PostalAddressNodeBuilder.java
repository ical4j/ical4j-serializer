package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.property.Address;

/**
 * Serializes an iCal4j {@link Entity} object representing a postal address to JSON-LD format.
 * This class extends {@link AbstractNodeBuilder} to provide custom serialization for postal address components.
 * <p>
 * The serialized output includes properties such as address locality, region, postal code, and street address.
 */
public class PostalAddressNodeBuilder extends AbstractNodeBuilder<Entity> {

    public PostalAddressNodeBuilder() {
        super("PostalAddress");
    }

    @Override
    public JsonNode build() {
        var node = createObjectNode();
        Address address = component.getRequiredProperty(PropertyName.ADR.toString());
        putIfNotNull("addressLocality", node, address.getLocality());
        putIfNotNull("addressRegion", node, address.getRegion());
        putIfNotNull("postalCode", node, address.getPostcode());
        putIfNotNull("streetAddress", node, address.getStreet());
        return node;
    }
}
