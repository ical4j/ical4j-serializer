package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VResource;
import org.mnode.ical4j.serializer.jotn.AbstractContentMapper;

import java.io.IOException;

public class VResourceMapper extends AbstractContentMapper<VResource> {

    public VResourceMapper(Class<VResource> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VResource deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new VResource());
    }
}
