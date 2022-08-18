package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

public class CardBuilder extends AbstractJotBuilder<VCard> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotAbsent("uid", node, component.getProperty(PropertyName.UID.toString()));
        return node;
    }
}
