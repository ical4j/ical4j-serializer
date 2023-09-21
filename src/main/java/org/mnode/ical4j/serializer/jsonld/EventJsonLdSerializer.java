package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VEvent;

public class EventJsonLdSerializer extends AbstractJsonLdSerializer<VEvent> {

    public EventJsonLdSerializer(Class<VEvent> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VEvent component) {
        AbstractJsonLdBuilder<VEvent> builder = new EventJsonLdBuilder().component(component);
        return builder.build();
    }
}
