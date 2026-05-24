package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VJournal;

import java.io.IOException;

/**
 * Serializer that emits a {@link VJournal} as a JSCalendar {@code jsevent} with
 * {@code freeBusyStatus:"free"} (RFC 8984 §5.1.4 convention).
 */
public class JSJournalSerializer extends StdSerializer<VJournal> {

    public JSJournalSerializer() {
        super(VJournal.class);
    }

    public JSJournalSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    public void serialize(VJournal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(new JSEventSerializer.JSJournalBuilder().component(value).build());
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }
}
