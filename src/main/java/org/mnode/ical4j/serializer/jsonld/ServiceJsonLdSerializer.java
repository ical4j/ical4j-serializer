package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VAvailability;

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
            putIfNotAbsent("@id", node, Property.UID);
            putIfNotAbsent("name", node, Property.SUMMARY);
            putIfNotAbsent("description", node, Property.DESCRIPTION);
            putIfNotAbsent("url", node, Property.URL);
            return node;
        }
    }
}
