package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VLocation;
import org.mnode.ical4j.serializer.jotn.AbstractContentMapper;

import java.io.IOException;

public class VLocationMapper extends AbstractContentMapper<VLocation> {

    public VLocationMapper(Class<VLocation> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VLocation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new VLocation());
    }
}
