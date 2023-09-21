package org.mnode.ical4j.serializer.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import net.fortuna.ical4j.model.component.VAvailability;

public class ServiceJsonLdSerializer extends AbstractJsonLdSerializer<VAvailability> {

    public ServiceJsonLdSerializer(Class<VAvailability> t) {
        super(t);
    }

    @Override
    protected JsonNode buildSchema(VAvailability component) {
        AbstractJsonLdBuilder<VAvailability> builder = new ServiceJsonLdBuilder().component(component);
        return builder.build();
    }
}
