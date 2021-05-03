package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;

public class EventBuilder extends AbstractSchemaBuilder {

    public EventBuilder uid(Uid uid) {
        this.uid = uid;
        return this;
    }

    public EventBuilder summary(Summary summary) {
        this.summary = summary;
        return this;
    }

    @Override
    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        node.put("@context", "https://schema.org");
        node.put("@type", "Event");
        setId(node, uid);
        setName(node, summary);
        return node;
    }
}
