package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;

/**
 * <a href="https://schema.org/Action">Action</a>
 */
public class ActionJsonLdBuilder extends AbstractJsonLdBuilder<VToDo> {

    public ActionJsonLdBuilder() {
        super("Action");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotAbsent("@id", node, component.getProperty(Property.UID));
        putIfNotAbsent("name", node, component.getProperty(Property.SUMMARY));
        putIfNotAbsent("description", node, component.getProperty(Property.DESCRIPTION));
        putIfNotAbsent("url", node, component.getProperty(Property.URL));
        putIfNotAbsent("startTime", node, component.getProperty(Property.DTSTART));
        putIfNotAbsent("endTime", node, component.getProperty(Property.DUE));
        putIfNotAbsent("actionStatus", node, component.getProperty(Property.STATUS));
        return node;
    }
}
