package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

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
            putIfNotAbsent("@id", node, Property.UID);
            putIfNotAbsent("name", node, Property.SUMMARY);
            putIfNotAbsent("description", node, Property.DESCRIPTION);
            putIfNotAbsent("url", node, Property.URL);
            putIfNotAbsent("startDate", node, Property.DTSTART);
            putIfNotAbsent("endDate", node, Property.DTEND);
            putIfNotAbsent("location", node, Property.LOCATION);
            return node;
        }
    }
}
