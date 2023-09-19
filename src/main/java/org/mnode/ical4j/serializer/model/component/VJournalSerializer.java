package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.VJournal;
import org.mnode.ical4j.serializer.model.AbstractJsonBuilder;
import org.mnode.ical4j.serializer.model.ComponentJsonBuilder;

import java.io.IOException;

/**
 * Convert iCal4j {@link VJournal} objects to JSON format.
 *
 * NOTE: Conversion to jot is "lossy" in that child components are ignored. This is intentional as
 * Jot JSON separates calendars and components into separate (not nested) JSON structures.
 */
public class VJournalSerializer extends StdSerializer<VJournal> {

    public VJournalSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    public void serialize(VJournal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildJournal(value));
    }

    private JsonNode buildJournal(VJournal journal) {
        AbstractJsonBuilder<VJournal> builder = new ComponentJsonBuilder<VJournal>().component(journal);
        return builder.build();
    }
}
