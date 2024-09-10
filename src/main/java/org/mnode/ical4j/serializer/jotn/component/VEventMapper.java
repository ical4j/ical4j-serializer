package org.mnode.ical4j.serializer.jotn.component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.data.DefaultParameterFactorySupplier;
import net.fortuna.ical4j.data.DefaultPropertyFactorySupplier;
import net.fortuna.ical4j.model.component.VEvent;
import org.mnode.ical4j.serializer.jotn.AbstractContentMapper;

import java.io.IOException;

public class VEventMapper extends AbstractContentMapper<VEvent> {

    public VEventMapper(Class<VEvent> t) {
        super(t);
        setPropertyFactories(new DefaultPropertyFactorySupplier().get());
        setParameterFactories(new DefaultParameterFactorySupplier().get());
    }

    @Override
    public VEvent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var event = map(p, new VEvent(false));

        // apply template if specified..
//        Optional<Concept> concept = event.getProperty("CONCEPT");
//        if (concept.isPresent()) {
//            try {
//                UnaryOperator<VEvent> template = new TemplateFactory().newInstance(concept.get());
//                template.apply(event);
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//        }
        return event;
    }
}
