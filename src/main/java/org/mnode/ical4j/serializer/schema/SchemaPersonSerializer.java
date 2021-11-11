package org.mnode.ical4j.serializer.schema;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.vcard.VCard;

public class SchemaPersonSerializer extends AbstractSchemaCardSerializer {

    public SchemaPersonSerializer(Class<VCard> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VCard card) {
        AbstractSchemaBuilder<VCard> builder = new SchemaPersonBuilder().component(card);
        return builder.build();
    }
}
