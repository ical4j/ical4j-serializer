package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VFreeBusy;
import org.mnode.ical4j.serializer.model.AbstractContentMapper;

import java.io.IOException;

public class VFreeBusyMapper extends AbstractContentMapper<VFreeBusy> {

    public VFreeBusyMapper(Class<VFreeBusy> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VFreeBusy deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new VFreeBusy(false));
    }
}
