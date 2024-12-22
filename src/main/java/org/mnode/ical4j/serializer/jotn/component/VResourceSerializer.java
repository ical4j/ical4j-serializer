package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VResource;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VResource} objects to JSON format.
 */
public class VResourceSerializer extends ContentSerializer<VResource> {

    /**
     * A subset of VRESOURCE properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public VResourceSerializer() {
        super(JOT_PROPS);
    }
}
