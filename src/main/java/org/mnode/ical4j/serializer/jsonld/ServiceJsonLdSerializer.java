package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAvailability;

/**
 * Serializes an iCal4j {@link VAvailability} object to JSON-LD format.
 * This class extends {@link AbstractJsonLdSerializer} to provide custom serialization for service components.
 * <p>
 * The serialized output includes properties such as UID, summary, description, and URL.
 */
public class ServiceJsonLdSerializer extends AbstractJsonLdSerializer<VAvailability> {

    public ServiceJsonLdSerializer(Class<VAvailability> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VAvailability component) {
        AbstractNodeBuilder<VAvailability> builder = new ServiceNodeBuilder().component(component);
        return builder.build();
    }

    public static class ServiceNodeBuilder extends AbstractNodeBuilder<VAvailability> {

        public ServiceNodeBuilder() {
            super("Service");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
            putIfNotAbsent("@id", node, component.getProperty(Property.UID));
            putIfNotAbsent("name", node, component.getProperty(Property.SUMMARY));
            putIfNotAbsent("description", node, component.getProperty(Property.DESCRIPTION));
            putIfNotAbsent("url", node, component.getProperty(Property.URL));
            return node;
        }
    }
}
