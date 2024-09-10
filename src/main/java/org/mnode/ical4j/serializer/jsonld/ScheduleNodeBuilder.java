package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VEvent;

public class ScheduleNodeBuilder extends AbstractNodeBuilder<VEvent> {

    public ScheduleNodeBuilder() {
        super("Schedule");
    }

    @Override
    public JsonNode build() {
        return createObjectNode();
    }
}
