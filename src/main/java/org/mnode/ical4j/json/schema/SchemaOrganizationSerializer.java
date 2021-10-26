package org.mnode.ical4j.json.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.VCard;

public class SchemaOrganizationSerializer extends AbstractSchemaCardSerializer {

    public SchemaOrganizationSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractSchemaBuilder<VCard> builder = new SchemaOrganizationBuilder().component(card);
        return builder.build();
    }
}
