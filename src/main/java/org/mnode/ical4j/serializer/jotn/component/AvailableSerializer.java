package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.Available;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Available} objects to JSON format.
 */
public class AvailableSerializer extends ContentSerializer<Available> {

    /**
     * A subset of AVAILABLE properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public AvailableSerializer() {
        super(JOT_PROPS);
    }
}
