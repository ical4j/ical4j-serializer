package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VJournal;

public class CreativeWorkJsonLdSerializer extends AbstractJsonLdSerializer<VJournal> {

    public CreativeWorkJsonLdSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VJournal component) {
        AbstractNodeBuilder<VJournal> builder = new CreativeWorkNodeBuilder().component(component);
        return builder.build();
    }

    public static class CreativeWorkNodeBuilder extends AbstractNodeBuilder<VJournal> {

        public CreativeWorkNodeBuilder() {
            super("CreativeWork");
        }

        @Override
        public JsonNode build() {
            ObjectNode node = createObjectNode();
            putIfNotAbsent("@id", node, Property.UID);
            putIfNotAbsent("name", node, Property.SUMMARY);
            putIfNotAbsent("description", node, Property.DESCRIPTION);
            putIfNotAbsent("url", node, Property.URL);
            return node;
        }
    }
}
