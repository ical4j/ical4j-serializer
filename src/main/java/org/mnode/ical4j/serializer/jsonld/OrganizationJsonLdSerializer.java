package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

public class OrganizationJsonLdSerializer extends AbstractJsonLdSerializer<VCard> {

    public OrganizationJsonLdSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractNodeBuilder<VCard> builder = new OrganizationNodeBuilder().component(card);
        return builder.build();
    }

    public static class OrganizationNodeBuilder extends AbstractNodeBuilder<VCard> {

        public OrganizationNodeBuilder() {
            super("Organization");
        }

        @Override
        public JsonNode build() {
            ObjectNode node = createObjectNode();
            putIfNotAbsent("@id", node, PropertyName.UID);
            putIfNotAbsent("name", node, PropertyName.FN);
            putIfNotAbsent("email", node, PropertyName.EMAIL);
            putIfNotAbsent("image", node, PropertyName.PHOTO);
            putIfNotAbsent("logo", node, PropertyName.LOGO);
            putIfNotAbsent("telephone", node, PropertyName.TEL);
            putIfNotAbsent("url", node, PropertyName.URL);
            setObject("address", node, PropertyName.ADR);
            setObject("member", node, PropertyName.MEMBER);
            return node;
        }
    }
}
