package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VFreeBusy;

public class FreebusyBuilder extends AbstractJotBuilder<VFreeBusy> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("uid", node, component.getProperty(Property.UID));
        return node;
    }
}
