package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;

public class CardBuilder extends AbstractJotBuilder<VCard> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotNull("uid", node, component.getProperty(Property.Id.UID));
        return node;
    }
}
