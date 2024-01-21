package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VAvailability;
import org.mnode.ical4j.serializer.model.AbstractContentMapper;

import java.io.IOException;

public class VAvailabilityMapper extends AbstractContentMapper<VAvailability> {

    public VAvailabilityMapper(Class<VAvailability> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VAvailability deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new VAvailability());
    }
}
