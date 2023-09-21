package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.VCard;

public class OrganizationJsonLdSerializer extends AbstractJsonLdSerializer<VCard> {

    public OrganizationJsonLdSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractJsonLdBuilder<VCard> builder = new OrganizationJsonLdBuilder().component(card);
        return builder.build();
    }
}
