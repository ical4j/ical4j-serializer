package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.component.VEvent;

public class JSEventBuilder extends AbstractJSCalendarBuilder<VEvent> {

    @Override
    public JsonNode build() {
        ObjectNode jsEvent = createObjectNode();
        jsEvent.put("@type", "jsevent");
        return jsEvent;
    }
}
