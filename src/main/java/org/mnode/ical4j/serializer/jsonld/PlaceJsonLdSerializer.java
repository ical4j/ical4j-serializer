package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;

public class PlaceJsonLdSerializer extends AbstractJsonLdSerializer<Entity> {

    public PlaceJsonLdSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(Entity card) {
        AbstractNodeBuilder<Entity> builder = new PlaceNodeBuilder().component(card);
        return builder.build();
    }

    public static class PlaceNodeBuilder extends AbstractNodeBuilder<Entity> {

        public PlaceNodeBuilder() {
            super("Place");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
            putIfNotAbsent("@id", node, PropertyName.UID);
            putIfNotAbsent("name", node, PropertyName.FN);
            putIfNotAbsent("image", node, PropertyName.PHOTO);
            putIfNotAbsent("url", node, PropertyName.URL);
            setObject("address", node, PropertyName.ADR);
            return node;
        }
    }
}
