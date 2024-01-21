package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

public class PlaceJsonLdSerializer extends AbstractJsonLdSerializer<VCard> {

    public PlaceJsonLdSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractNodeBuilder<VCard> builder = new PlaceNodeBuilder().component(card);
        return builder.build();
    }

    public static class PlaceNodeBuilder extends AbstractNodeBuilder<VCard> {

        public PlaceNodeBuilder() {
            super("Place");
        }

        @Override
        public JsonNode build() {
            ObjectNode node = createObjectNode();
            putIfNotAbsent("@id", node, PropertyName.UID);
            putIfNotAbsent("name", node, PropertyName.FN);
            putIfNotAbsent("image", node, PropertyName.PHOTO);
            putIfNotAbsent("url", node, PropertyName.URL);
            setObject("address", node, PropertyName.ADR);
            return node;
        }
    }
}
