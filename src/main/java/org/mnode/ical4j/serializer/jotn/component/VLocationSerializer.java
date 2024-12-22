package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VLocation;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VLocation} objects to JSON format.
 */
public class VLocationSerializer extends ContentSerializer<VLocation> {

    /**
     * A subset of VLOCATION properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public VLocationSerializer() {
        super(JOT_PROPS);
    }
}
