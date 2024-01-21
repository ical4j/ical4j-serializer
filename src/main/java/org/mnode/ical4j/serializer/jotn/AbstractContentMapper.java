package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterFactory;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyFactory;

import java.util.List;

public abstract class AbstractContentMapper<T> extends StdDeserializer<T> implements JsonObjectMapper {

    private List<PropertyFactory<? extends Property>> propertyFactories;

    private List<ParameterFactory<? extends Parameter>> parameterFactories;

    public AbstractContentMapper(Class<T> vc) {
        super(vc);
    }

    @Override
    public List<ParameterFactory<?>> getParameterFactories() {
        return parameterFactories;
    }

    @Override
    public List<PropertyFactory<?>> getPropertyFactories() {
        return propertyFactories;
    }

    public void setPropertyFactories(List<PropertyFactory<?>> propertyFactories) {
        this.propertyFactories = propertyFactories;
    }

    public void setParameterFactories(List<ParameterFactory<?>> parameterFactories) {
        this.parameterFactories = parameterFactories;
    }
}
