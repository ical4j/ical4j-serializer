package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;

public class JSGroupBuilder extends AbstractJSCalendarBuilder<Calendar> {

    @Override
    public JsonNode build() {
        ObjectNode jsEvent = createObjectNode();
        jsEvent.put("@type", "jsgroup");
        putIfNotNull("prodid", jsEvent, component.getProperty(Property.PRODID));
        putIfNotNull("uid", jsEvent, component.getProperty(Property.UID));
        return jsEvent;
    }
}
