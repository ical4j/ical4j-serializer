package org.mnode.ical4j.serializer.jotn.component;

import net.fortuna.ical4j.model.component.Participant;
import org.mnode.ical4j.serializer.jotn.ContentSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * Convert iCal4j {@link Participant} objects to JSON format.
 */
public class ParticipantSerializer extends ContentSerializer<Participant> {

    /**
     * A subset of PARTICIPANT properties supported by JOT notation.
     */
    private static final List<String> JOT_PROPS = Arrays.asList("UID");

    public ParticipantSerializer() {
        super(JOT_PROPS);
    }
}
