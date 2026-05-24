package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.Entity;
import org.mnode.ical4j.serializer.JsonBuilder;

/**
 * Abstract base class for building JSContact objects in JSON format.
 * This class provides a common structure for contact builders that
 * serialize vCard {@link Entity} instances into JSON nodes.
 */
public abstract class AbstractJSContactBuilder implements JsonBuilder {

    private final String objectType;

    protected Entity component;

    protected AbstractJSContactBuilder(String objectType) {
        this.objectType = objectType;
    }

    public AbstractJSContactBuilder component(Entity component) {
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
     * Build a JSON node representing the JSContact object.
     * @return a JSON representation of a JSContact object
     */
    public abstract JsonNode build();
}
