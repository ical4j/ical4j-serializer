package org.mnode.ical4j.serializer.jot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import net.fortuna.ical4j.model.component.VJournal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public class JotJournalMapper extends AbstractJotCalMapper<VJournal> {

    public JotJournalMapper(Class<VJournal> t) {
        super(t);
    }

    @Override
    public VJournal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VJournal journal = new VJournal(false);
        assertCurrentToken(p, JsonToken.START_OBJECT);
        while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
            assertCurrentToken(p, JsonToken.FIELD_NAME);
            String propertyName = p.currentName();
            try {
                if (JsonToken.START_ARRAY.equals(p.nextToken())) {
                    journal.getProperties().addAll(parsePropertyList(propertyName, p));
                } else {
                    journal.getProperties().add(parseProperty(propertyName, p));
                }
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return journal;
    }
}
