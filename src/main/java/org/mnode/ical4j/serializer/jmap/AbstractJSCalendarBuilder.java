package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mnode.ical4j.serializer.JsonBuilder;

/**
 * Abstract base class for building JSCalendar objects in JSON format.
 * This class provides a common structure for calendar builders that
 * serialize components into JSON nodes.
 *
 * @param <T> the type of component to be serialized
 */
public abstract class AbstractJSCalendarBuilder<T> implements JsonBuilder {

    private final String objectType;

    protected T component;

    public AbstractJSCalendarBuilder(String objectType) {
        this.objectType = objectType;
    }

    public AbstractJSCalendarBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        var mapper = new ObjectMapper();

        var node = mapper.createObjectNode();
        node.put("@type", objectType);
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
