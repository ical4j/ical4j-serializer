package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.PropertyListAccessor;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.property.Address;
import org.mnode.ical4j.serializer.JsonBuilder;

import java.util.Optional;

/**
 * Abstract base class for building JSON-LD nodes from iCal4j components.
 * This class provides common functionality for creating JSON nodes and managing the component properties.
 *
 * @param <T> the type of component to be built into a JSON-LD node, extending {@link PropertyListAccessor}
 */
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
        var mapper = new ObjectMapper();

        var node = mapper.createObjectNode();
        node.put("@context", "https://schema.org");
        node.put("@type", schemaType);
        return node;
    }

    protected ObjectNode setObject(String propertyName, ObjectNode node, String property) {
        Optional<?> prop = component.getProperty(property);
        if (prop.isPresent() && prop.get() instanceof Address) {
            JsonNode address = new PostalAddressNodeBuilder().component((Entity) component).build();
            node.set(propertyName, address);
        }
        return node;
    }

    protected ObjectNode setObject(String propertyName, ObjectNode node, Enum<?> property) {
        Optional<?> prop = component.getProperty(property);
        if (prop.isPresent() && prop.get() instanceof Address) {
            JsonNode address = new PostalAddressNodeBuilder().component((Entity) component).build();
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
