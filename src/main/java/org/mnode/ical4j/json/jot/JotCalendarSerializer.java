package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;

/**
 * Convert iCal4j {@link Calendar} objects to Jot JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class JotCalendarSerializer extends StdSerializer<Calendar> {

    public JotCalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCalendar(value));
    }

    private JsonNode buildCalendar(Calendar calendar) {
        AbstractJotBuilder<Calendar> builder = new CalendarBuilder().component(calendar);
        return builder.build();
    }
}
