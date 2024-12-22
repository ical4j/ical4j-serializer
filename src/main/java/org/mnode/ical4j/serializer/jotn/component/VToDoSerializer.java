package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VToDo;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VToDo} objects to JSON format.
 *
 * NOTE: Model conversion to JSON is "lossy" in that child components are ignored. This is intentional as
 * model JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VToDoSerializer extends ContentSerializer<VToDo> {

    /**
     * A subset of VTODO properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY", "SUMMARY");

    public VToDoSerializer() {
        super(JOT_PROPS);
    }
}
