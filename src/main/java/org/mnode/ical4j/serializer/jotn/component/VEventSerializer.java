package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VEvent;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VEvent} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VEventSerializer extends ContentSerializer<VEvent> {

    /**
     * A subset of VEVENT properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY",
            "DTSTART", "SUMMARY", "CATEGORIES", "DESCRIPTION", "RECURRENCE-ID", "CONCEPT");

    public VEventSerializer() {
        super(JOT_PROPS);
    }
}
