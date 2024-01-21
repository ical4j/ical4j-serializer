package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Calendar} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class CalendarSerializer extends StdSerializer<Calendar> {

    /**
     * A subset of calendar properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID", "LAST-MODIFIED", "URL", "REFRESH",
            "SOURCE", "COLOR", "NAME", "DESCRIPTION", "CATEGORIES", "IMAGE");

    public CalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCalendar(value));
    }

    private JsonNode buildCalendar(Calendar calendar) {
        JsonObjectBuilder builder = new JsonObjectBuilder(calendar, JOT_PROPS);
        return builder.build();
    }
}
