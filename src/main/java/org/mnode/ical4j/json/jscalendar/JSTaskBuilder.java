package org.mnode.ical4j.json.jscalendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class JSTaskBuilder extends AbstractJSCalendarBuilder {

    private LocalDateTime due;
    
    private LocalDateTime start;

    private Duration estimatedDuration;

    private Integer percentComplete;

    private String progress; // completed/failed/in-process/needs-action/cancelled

    private Instant progressUpdated;

    public JSTaskBuilder uid(String uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode jsEvent = mapper.createObjectNode();
        jsEvent.put("@type", "jstask");
        return jsEvent;
    }
}
