package org.mnode.ical4j.json.jscontact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSCardBuilder extends AbstractJSContactBuilder {

    @Override
    public JsonNode build() {
        ObjectNode card = createObjectNode();
        card.put("@type", "jsevent");
        return card;
    }
}
