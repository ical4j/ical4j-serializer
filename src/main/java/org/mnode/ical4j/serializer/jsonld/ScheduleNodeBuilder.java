package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.component.VEvent;

public class ScheduleNodeBuilder extends AbstractNodeBuilder<VEvent> {

    public ScheduleNodeBuilder() {
        super("Schedule");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        return node;
    }
}
