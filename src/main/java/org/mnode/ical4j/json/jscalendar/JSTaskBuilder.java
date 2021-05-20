package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.component.VToDo;

public class JSTaskBuilder extends AbstractJSCalendarBuilder<VToDo> {

    @Override
    public JsonNode build() {
        ObjectNode jsEvent = createObjectNode();
        jsEvent.put("@type", "jstask");
        return jsEvent;
    }
}
