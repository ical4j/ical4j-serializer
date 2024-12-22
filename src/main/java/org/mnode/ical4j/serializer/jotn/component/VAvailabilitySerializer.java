package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VAvailability;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VAvailability} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VAvailabilitySerializer extends ContentSerializer<VAvailability> {

    /**
     * A subset of VAVAILABILITY properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public VAvailabilitySerializer() {
        super(JOT_PROPS);
    }
}
