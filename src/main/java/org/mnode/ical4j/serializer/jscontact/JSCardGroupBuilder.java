package org.mnode.ical4j.serializer.jscontact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSCardGroupBuilder extends AbstractJSContactBuilder {

    @Override
    public JsonNode build() {
        ObjectNode card = createObjectNode();
        card.put("@type", "jsevent");
        return card;
    }
}
