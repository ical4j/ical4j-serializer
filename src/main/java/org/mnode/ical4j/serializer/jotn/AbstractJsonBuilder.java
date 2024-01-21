package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mnode.ical4j.serializer.JsonBuilder;

/**
 * Build JSON objects from iCalendar and vCard objects.
 * @param <T>
 */
public abstract class AbstractJsonBuilder<T> implements JsonBuilder {

    protected T component;

    public AbstractJsonBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
