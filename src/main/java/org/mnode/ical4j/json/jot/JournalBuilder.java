package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.property.Uid;

public class JournalBuilder extends AbstractJotBuilder {

    public JournalBuilder uid(Uid uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public JsonNode build() {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode node = mapper.createObjectNode();
        setUid(node, uid);
        return node;
    }
}