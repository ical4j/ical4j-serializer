package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.VCard;

public class PlaceJsonLdSerializer extends AbstractJsonLdSerializer<VCard> {

    public PlaceJsonLdSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractJsonLdBuilder<VCard> builder = new PlaceJsonLdBuilder().component(card);
        return builder.build();
    }
}
