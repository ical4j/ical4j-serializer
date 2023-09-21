package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.vcard.PropertyName;
import net.fortuna.ical4j.vcard.VCard;

public class OrganizationJsonLdBuilder extends AbstractJsonLdBuilder<VCard> {

    public OrganizationJsonLdBuilder() {
        super("Organization");
    }

    @Override
    public JsonNode build() {
        ObjectNode node = createObjectNode();
        putIfNotAbsent("@id", node, component.getProperty(PropertyName.UID.toString()));
        putIfNotAbsent("name", node, component.getProperty(PropertyName.FN.toString()));
        putIfNotAbsent("email", node, component.getProperty(PropertyName.EMAIL.toString()));
        putIfNotAbsent("image", node, component.getProperty(PropertyName.PHOTO.toString()));
        putIfNotAbsent("logo", node, component.getProperty(PropertyName.LOGO.toString()));
        putIfNotAbsent("telephone", node, component.getProperty(PropertyName.TEL.toString()));
        putIfNotAbsent("url", node, component.getProperty(PropertyName.URL.toString()));
        setObject("address", node, component.getProperty(PropertyName.ADR.toString()));
        setObject("member", node, component.getProperty(PropertyName.MEMBER.toString()));
        return node;
    }
}
