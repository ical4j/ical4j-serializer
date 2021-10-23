package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.component.VToDo;

public class JSTaskBuilder extends AbstractJSCalendarBuilder<VToDo> {

    public JSTaskBuilder() {
        super("jstask");
    }

    @Override
    public JsonNode build() {
        ObjectNode jsTask = createObjectNode();
        return jsTask;
    }
}
