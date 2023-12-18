package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.PropertyListAccessor;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.property.Address;
import org.mnode.ical4j.serializer.JsonBuilder;

import java.util.Optional;

public abstract class AbstractNodeBuilder<T extends PropertyListAccessor> implements JsonBuilder {

    private final String schemaType;

    protected T component;

    public AbstractNodeBuilder(String schemaType) {
        this.schemaType = schemaType;
    }

    public AbstractNodeBuilder<T> component(T component) {
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

    protected ObjectNode putIfNotAbsent(String propertyName, ObjectNode node, String property) {
        component.getProperty(property).ifPresent(value -> node.put(propertyName, value.getValue()));
        return node;
    }

    protected ObjectNode putIfNotAbsent(String propertyName, ObjectNode node, Enum<?> property) {
        component.getProperty(property).ifPresent(value -> node.put(propertyName, value.getValue()));
        return node;
    }

    protected ObjectNode setObject(String propertyName, ObjectNode node, String property) {
        Optional<?> prop = component.getProperty(property);
        if (prop.isPresent() && prop.get() instanceof Address) {
            JsonNode address = new PostalAddressNodeBuilder().component((VCard) component).build();
            node.set(propertyName, address);
        }
        return node;
    }

    protected ObjectNode setObject(String propertyName, ObjectNode node, Enum<?> property) {
        Optional<?> prop = component.getProperty(property);
        if (prop.isPresent() && prop.get() instanceof Address) {
            JsonNode address = new PostalAddressNodeBuilder().component((VCard) component).build();
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
