package org.mnode.ical4j.serializer.model.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.component.Participant;
import org.mnode.ical4j.serializer.model.JsonObjectBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Participant} objects to JSON format.
 */
public class ParticipantSerializer extends StdSerializer<Participant> {

    /**
     * A subset of PARTICIPANT properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public ParticipantSerializer(Class<Participant> t) {
        super(t);
    }

    @Override
    public void serialize(Participant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildParticipant(value));
    }

    private JsonNode buildParticipant(Participant participant) {
        JsonObjectBuilder builder = new JsonObjectBuilder(participant, JOT_PROPS);
        return builder.build();
    }
}
