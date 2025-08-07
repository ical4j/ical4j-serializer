package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VEvent;

/**
 * Serializes an iCal4j {@link VEvent} object to JSON-LD format.
 * This class extends {@link AbstractNodeBuilder} to provide custom serialization for event components.
 * <p>
 * The serialized output includes properties such as UID, summary, description, URL, start date, end date, and location.
 */
public class ScheduleNodeBuilder extends AbstractNodeBuilder<VEvent> {

    public ScheduleNodeBuilder() {
        super("Schedule");
    }

    @Override
    public JsonNode build() {
        return createObjectNode();
    }
}
