package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

/**
 * Serializes an iCal4j {@link VEvent} object to JSON-LD format.
 * This class extends {@link AbstractJsonLdSerializer} to provide custom serialization for event components.
 * <p>
 * The serialized output includes properties such as UID, summary, description, URL, start date, end date, and location.
 */
public class EventJsonLdSerializer extends AbstractJsonLdSerializer<VEvent> {

    public EventJsonLdSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VEvent component) {
        AbstractNodeBuilder<VEvent> builder = new EventNodeBuilder().component(component);
        return builder.build();
    }

    public static class EventNodeBuilder extends AbstractNodeBuilder<VEvent> {

        public EventNodeBuilder() {
            super("Event");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
            putIfNotAbsent("@id", node, component.getProperty(Property.UID));
            putIfNotAbsent("name", node, component.getProperty(Property.SUMMARY));
            putIfNotAbsent("description", node, component.getProperty(Property.DESCRIPTION));
            putIfNotAbsent("url", node, component.getProperty(Property.URL));
            putIfNotAbsent("startDate", node, component.getProperty(Property.DTSTART));
            putIfNotAbsent("endDate", node, component.getProperty(Property.DTEND));
            putIfNotAbsent("location", node, component.getProperty(Property.LOCATION));
            return node;
        }
    }
}
