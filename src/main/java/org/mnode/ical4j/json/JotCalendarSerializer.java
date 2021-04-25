package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Uid;
import org.mnode.ical4j.json.jot.CalendarBuilder;

import java.io.IOException;

public class JotCalendarSerializer extends StdSerializer<Calendar> {

    public JotCalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCalendar(value));
    }

    private JsonNode buildCalendar(Calendar calendar) {
        Uid uid = calendar.getProperty(Property.UID);
        CalendarBuilder builder = new CalendarBuilder().uid(uid);

        return builder.build();
    }
}
