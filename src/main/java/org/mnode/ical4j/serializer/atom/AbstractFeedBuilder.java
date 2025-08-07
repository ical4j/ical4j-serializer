package org.mnode.ical4j.serializer.atom;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.mnode.ical4j.serializer.JsonBuilder;

/**
 * Abstract base class for building Atom feeds in JSON format.
 * This class provides a common structure for feed builders that
 * serialize components into JSON nodes.
 *
 * @param <T> the type of component to be serialized
 */
public abstract class AbstractFeedBuilder<T> implements JsonBuilder {

    protected T component;

    protected final ObjectMapper mapper;

    public AbstractFeedBuilder() {
        this.mapper = new XmlMapper();
    }

    public AbstractFeedBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    /**
     * Build a JSON node representing the Jsonfeed object.
     * @return a JSON representation of a Jsonfeed object
     */
    public abstract JsonNode build();
}
