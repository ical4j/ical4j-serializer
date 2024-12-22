package org.mnode.ical4j.serializer.jsonfeed;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mnode.ical4j.serializer.JsonBuilder;

public abstract class AbstractFeedBuilder<T> implements JsonBuilder {

    private final String version;

    protected T component;

    protected final ObjectMapper mapper;

    public AbstractFeedBuilder() {
        this("https://jsonfeed.org/version/1.1");
    }

    public AbstractFeedBuilder(String version) {
        this.version = version;
        this.mapper = new ObjectMapper();
    }

    public AbstractFeedBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        var node = mapper.createObjectNode();
        node.put("version", version);
        node.putArray("items");
        return node;
    }

    /**
     * Build a JSON node representing the Jsonfeed object.
     * @return a JSON representation of a Jsonfeed object
     */
    public abstract JsonNode build();
}
