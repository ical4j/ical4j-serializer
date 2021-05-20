package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.property.Address;
import org.mnode.ical4j.json.JsonBuilder;

public abstract class AbstractSchemaBuilder<T> implements JsonBuilder {

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

    protected ObjectNode setObject(String propertyName, ObjectNode node, net.fortuna.ical4j.vcard.Property property) {
        if (property instanceof Address) {
            JsonNode address = new SchemaPostalAddressBuilder().component((Address) property).build();
            node.set(propertyName, address);
        }
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
