package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.VCard;
import org.mnode.ical4j.serializer.JsonBuilder;

public abstract class AbstractJSContactBuilder implements JsonBuilder {

    protected VCard component;

    public AbstractJSContactBuilder component(VCard component) {
        this.component = component;
        return this;
    }

    protected ObjectNode createObjectNode() {
        var mapper = new ObjectMapper();

        var node = mapper.createObjectNode();
        return node;
    }

    /**
     * Build a JSON node representing the JSCalendar object.
     * @return a JSON representation of a JSCalendar object
     */
    public abstract JsonNode build();
}
