package org.mnode.ical4j.serializer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

public class VCardJsonBuilder extends AbstractJsonBuilder<VCard> {

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotAbsent("uid", node, component.getProperty(PropertyName.UID.toString()));
        return node;
    }
}
