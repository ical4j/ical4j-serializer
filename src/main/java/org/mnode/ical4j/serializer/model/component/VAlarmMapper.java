package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VAlarm;
import org.mnode.ical4j.serializer.model.AbstractContentMapper;

import java.io.IOException;

public class VAlarmMapper extends AbstractContentMapper<VAlarm> {

    public VAlarmMapper(Class<VAlarm> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VAlarm deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return map(p, new VAlarm());
    }
}
