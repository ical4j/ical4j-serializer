package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.PropertyName;

/**
 * Serializes an iCal4j {@link Entity} object representing a place to JSON-LD format.
 * This class extends {@link AbstractJsonLdSerializer} to provide custom serialization for place components.
 * <p>
 * The serialized output includes properties such as UID, name, image, URL, and address.
 */
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
            putIfNotAbsent("@id", node, component.getProperty(PropertyName.UID));
            putIfNotAbsent("name", node, component.getProperty(PropertyName.FN));
            putIfNotAbsent("image", node, component.getProperty(PropertyName.PHOTO));
            putIfNotAbsent("url", node, component.getProperty(PropertyName.URL));
            setObject("address", node, PropertyName.ADR);
            return node;
        }
    }
}
