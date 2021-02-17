package org.mnode.ical4j.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class JSGroupBuilder extends AbstractJSCalendarBuilder {

    private List<AbstractJSCalendarBuilder> entries;

    private String source;

    public JSGroupBuilder uid(String uid) {
        this.uid = uid;
        return this;
    }

    public JSGroupBuilder prodId(String prodId) {
        this.prodId = prodId;
        return this;
    }

    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsEvent = mapper.createObjectNode();
        jsEvent.put("@type", "jsgroup");
        setProperty(jsEvent, "uid", uid, true);
        setProperty(jsEvent, "prodId", prodId, false);
        return jsEvent;
    }
}
