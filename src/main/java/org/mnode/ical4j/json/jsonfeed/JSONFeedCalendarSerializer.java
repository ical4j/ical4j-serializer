package org.mnode.ical4j.json.jsonfeed;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Calendar;

import java.io.IOException;

public class JSONFeedCalendarSerializer extends StdSerializer<Calendar> {

    public JSONFeedCalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    @Override
    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildCalendar(value));
    }

    private JsonNode buildCalendar(Calendar calendar) {
        AbstractFeedBuilder<Calendar> builder = new CalendarBuilder().component(calendar);
        return builder.build();
    }
}
