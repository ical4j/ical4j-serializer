package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.VAlarm;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link VAlarm} objects to JSON format.
 */
public class VAlarmSerializer extends ContentSerializer<VAlarm> {

    /**
     * A subset of VALARM properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "ORGANIZER", "LOCATION", "RESOURCES",
            "ATTACH", "RELATED-TO", "ATTENDEE", "TRIGGER", "COMMENT", "CONTACT", "FREEBUSY");

    public VAlarmSerializer() {
        super(JOT_PROPS);
    }
}
