package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VJournal;

public class CreativeWorkJsonLdSerializer extends AbstractJsonLdSerializer<VJournal> {

    public CreativeWorkJsonLdSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VJournal component) {
        AbstractJsonLdBuilder<VJournal> builder = new CreativeWorkJsonLdBuilder().component(component);
        return builder.build();
    }
}
