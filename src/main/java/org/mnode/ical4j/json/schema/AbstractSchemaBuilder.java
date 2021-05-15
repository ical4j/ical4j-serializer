package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;

public abstract class AbstractSchemaBuilder<T> {

    private final String schemaType;

    protected T component;

    public AbstractSchemaBuilder(String schemaType) {
        this.schemaType = schemaType;
    }

    public AbstractSchemaBuilder<T> component(T component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        node.put("@context", "https://schema.org");
        node.put("@type", schemaType);
        return node;
    }

    protected ObjectNode setProperty(String propertyName, ObjectNode node, Property property) {
        if (property != null) {
            node.put(propertyName, property.getValue());
        }
        return node;
    }

    protected ObjectNode setProperty(String propertyName, ObjectNode node, net.fortuna.ical4j.vcard.Property property) {
        if (property != null) {
            node.put(propertyName, property.getValue());
        }
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
