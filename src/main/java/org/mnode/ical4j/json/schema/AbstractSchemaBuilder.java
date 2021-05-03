package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

public abstract class AbstractSchemaBuilder {

    protected Uid uid;

    protected Summary summary;

    protected ObjectNode setId(ObjectNode node, Uid uid) {
        node.put("@id", uid.getValue());
        return node;
    }

    protected ObjectNode setName(ObjectNode node, Summary summary) {
        node.put("name", summary.getValue());
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
