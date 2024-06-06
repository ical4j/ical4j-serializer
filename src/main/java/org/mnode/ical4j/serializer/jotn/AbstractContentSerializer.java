package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.PropertyContainer;

import java.util.List;

public abstract class AbstractContentSerializer<T extends PropertyContainer> extends StdSerializer<T> {

    private final List<String> filteredProps;

    public AbstractContentSerializer(Class<T> t, List<String> filteredProps) {
        super(t);
        this.filteredProps = filteredProps;
    }

    protected JsonObjectBuilder newBuilder(T instance) {
        return new JsonObjectBuilder(instance, filteredProps);
    }
}
