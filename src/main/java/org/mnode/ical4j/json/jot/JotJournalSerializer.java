package org.mnode.ical4j.json.jot;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VJournal;
import net.fortuna.ical4j.model.property.Uid;

import java.io.IOException;

public class JotJournalSerializer extends StdSerializer<VJournal> {

    public JotJournalSerializer(Class<VJournal> t) {
        super(t);
    }

    @Override
    public void serialize(VJournal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildJournal(value));
    }

    private JsonNode buildJournal(VJournal journal) {
        Uid uid = journal.getProperty(Property.UID);
        JournalBuilder builder = new JournalBuilder().uid(uid);

        return builder.build();
    }
}
