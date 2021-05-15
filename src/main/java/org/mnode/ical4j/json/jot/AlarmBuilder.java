package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.*;

import java.util.ArrayList;
import java.util.List;

public class AlarmBuilder extends AbstractJotBuilder {

    private Action action;

    private Trigger trigger;

    private Description description;

    private Duration duration;

    private Repeat repeat;

    private Summary summary;

    private List<Attendee> attendeeList = new ArrayList<>();

    private List<Attach> attachList = new ArrayList<>();

    public AlarmBuilder uid(Uid uid) {
        this.uid = uid;
        return this;
    }

    public AlarmBuilder action(Action action) {
        this.action = action;
        return this;
    }

    public AlarmBuilder trigger(Trigger trigger) {
        this.trigger = trigger;
        return this;
    }

    private ObjectNode setAction(ObjectNode node, Action action) {
        node.put("action", action.getValue());
        return node;
    }

    private ObjectNode setTrigger(ObjectNode node, Trigger trigger) {
        node.put("trigger", trigger.getValue());
        return node;
    }

    @Override
    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        setUid(node, uid);
        setAction(node, action);
        setTrigger(node, trigger);
        return node;
    }
}
