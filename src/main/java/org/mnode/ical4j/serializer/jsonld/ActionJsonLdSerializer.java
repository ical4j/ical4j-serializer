package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
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
        AbstractJsonLdBuilder<VToDo> builder = new ActionJsonLdBuilder().component(component);
        return builder.build();
    }
}
