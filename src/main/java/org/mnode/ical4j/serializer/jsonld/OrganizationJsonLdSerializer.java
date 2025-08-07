package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;

/**
 * Serializes an iCal4j {@link Entity} object representing an organization to JSON-LD format.
 * This class extends {@link AbstractJsonLdSerializer} to provide custom serialization for organization components.
 * <p>
 * The serialized output includes properties such as UID, name, email, image, logo, telephone, URL, address, and members.
 */
public class OrganizationJsonLdSerializer extends AbstractJsonLdSerializer<Entity> {

    public OrganizationJsonLdSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(Entity entity) {
        AbstractNodeBuilder<Entity> builder = new OrganizationNodeBuilder().component(entity);
        return builder.build();
    }

    public static class OrganizationNodeBuilder extends AbstractNodeBuilder<Entity> {

        public OrganizationNodeBuilder() {
            super("Organization");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
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
