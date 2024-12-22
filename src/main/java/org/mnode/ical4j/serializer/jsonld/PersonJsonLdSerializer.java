package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;

public class PersonJsonLdSerializer extends AbstractJsonLdSerializer<Entity> {

    public PersonJsonLdSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(Entity card) {
        AbstractNodeBuilder<Entity> builder = new PersonNodeBuilder().component(card);
        return builder.build();
    }

    public static class PersonNodeBuilder extends AbstractNodeBuilder<Entity> {

        public PersonNodeBuilder() {
            super("Person");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
            putIfNotAbsent("@id", node, PropertyName.UID);
            putIfNotAbsent("name", node, PropertyName.FN);
            putIfNotAbsent("email", node, PropertyName.EMAIL);
            putIfNotAbsent("image", node, PropertyName.PHOTO);
            putIfNotAbsent("jobTitle", node, PropertyName.TITLE);
            putIfNotAbsent("telephone", node, PropertyName.TEL);
            putIfNotAbsent("url", node, PropertyName.URL);
            setObject("address", node, PropertyName.ADR);
            return node;
        }
    }
}
