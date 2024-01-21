package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.Available;
import org.mnode.ical4j.serializer.model.AbstractContentMapper;

import java.io.IOException;

public class AvailableMapper extends AbstractContentMapper<Available> {

    public AvailableMapper(Class<Available> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public Available deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new Available());
    }
}
