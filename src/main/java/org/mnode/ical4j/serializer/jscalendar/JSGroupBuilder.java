package org.mnode.ical4j.serializer.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;

public class JSGroupBuilder extends AbstractJSCalendarBuilder<Calendar> {

    public JSGroupBuilder() {
        super("jsgroup");
    }

    @Override
    public JsonNode build() {
        ObjectNode jsGroup = createObjectNode();
        putIfNotNull("prodid", jsGroup, component.getProperty(Property.PRODID));
        putIfNotNull("uid", jsGroup, component.getProperty(Property.UID));
        return jsGroup;
    }
}
