package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAvailability;

public class ServiceJsonLdBuilder extends AbstractJsonLdBuilder<VAvailability> {

    public ServiceJsonLdBuilder() {
        super("Service");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotAbsent("@id", node, component.getProperty(Property.UID));
        putIfNotAbsent("name", node, component.getProperty(Property.SUMMARY));
        putIfNotAbsent("description", node, component.getProperty(Property.DESCRIPTION));
        putIfNotAbsent("url", node, component.getProperty(Property.URL));
        return node;
    }
}
