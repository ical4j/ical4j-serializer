package org.mnode.ical4j.serializer.jotn;

import net.fortuna.ical4j.model.Calendar;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Calendar} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class CalendarSerializer extends ContentSerializer<Calendar> {

    /**
     * A subset of calendar properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "LAST-MODIFIED", "URL", "REFRESH",
            "SOURCE", "COLOR", "NAME", "DESCRIPTION", "CATEGORIES", "IMAGE");

    public CalendarSerializer() {
        super(JOT_PROPS);
    }

    public CalendarSerializer(String...filteredProps) {
        super(Arrays.asList(filteredProps));
    }
}
