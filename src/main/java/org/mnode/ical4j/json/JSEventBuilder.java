package org.mnode.ical4j.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;
import java.time.LocalDateTime;

public class JSEventBuilder extends AbstractJSCalendarBuilder {

    private LocalDateTime start;

    private Duration duration;

    private String status;

    JSEventBuilder uid(String uid) {
        this.uid = uid;
        return this;
    }

    JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsEvent = mapper.createObjectNode();
        jsEvent.put("@type", "jsevent");
        return jsEvent;
    }
}
