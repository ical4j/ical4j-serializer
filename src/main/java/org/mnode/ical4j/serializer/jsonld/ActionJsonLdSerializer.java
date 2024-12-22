package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;

/**
 * <a href="https://schema.org/Action">Action</a>
 *
 */
public class ActionJsonLdSerializer extends AbstractJsonLdSerializer<VToDo> {

    public ActionJsonLdSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VToDo component) {
        AbstractNodeBuilder<VToDo> builder = new ActionNodeBuilder().component(component);
        return builder.build();
    }

    /**
     * <a href="https://schema.org/Action">Action</a>
     */
    public static class ActionNodeBuilder extends AbstractNodeBuilder<VToDo> {

        public ActionNodeBuilder() {
            super("Action");
        }

        @Override
        public JsonNode build() {
            var node = createObjectNode();
            putIfNotAbsent("@id", node, Property.UID);
            putIfNotAbsent("name", node, Property.SUMMARY);
            putIfNotAbsent("description", node, Property.DESCRIPTION);
            putIfNotAbsent("url", node, Property.URL);
            putIfNotAbsent("startTime", node, Property.DTSTART);
            putIfNotAbsent("endTime", node, Property.DUE);
            putIfNotAbsent("actionStatus", node, Property.STATUS);
            return node;
        }
    }
}
