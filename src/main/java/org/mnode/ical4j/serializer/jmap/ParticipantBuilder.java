package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Organizer;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Builds the JSCalendar {@code participants} map from a component's
 * {@code ORGANIZER} and {@code ATTENDEE} properties.
 */
final class ParticipantBuilder {

    private ParticipantBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        Optional<Organizer> organizer = source.getProperty("ORGANIZER");
        List<Attendee> attendees = source.getProperties("ATTENDEE");
        if (organizer.isEmpty() && attendees.isEmpty()) {
            return;
        }
        Map<String, ObjectNode> participants = new LinkedHashMap<>();
        organizer.ifPresent(value -> mergeParticipant(value, value.getCalAddress(), participants, target, true));
        for (Attendee attendee : attendees) {
            mergeParticipant(attendee, attendee.getCalAddress(), participants, target, false);
        }
        ObjectNode node = target.objectNode();
        participants.forEach(node::set);
        target.set("participants", node);
    }

    private static void mergeParticipant(Property property, URI calAddress, Map<String, ObjectNode> participants,
                                         ObjectNode parent, boolean owner) {
        String key = participantKey(calAddress, property.getValue());
        ObjectNode participant = participants.computeIfAbsent(key, k -> {
            ObjectNode node = parent.objectNode();
            node.put("@type", "Participant");
            ObjectNode sendTo = parent.objectNode();
            sendTo.put("imip", "mailto:" + k);
            node.set("sendTo", sendTo);
            node.set("roles", parent.objectNode());
            return node;
        });

        ObjectNode roles = (ObjectNode) participant.get("roles");
        if (owner) {
            roles.put("owner", true);
        }
        roles.put("attendee", true);
        Optional<Role> role = property.getParameter(Parameter.ROLE);
        role.ifPresent(value -> {
            if (Role.CHAIR.getValue().equalsIgnoreCase(value.getValue())) {
                roles.put("chair", true);
            }
        });
    }

    private static String participantKey(URI calAddress, String rawValue) {
        String address = calAddress != null ? calAddress.toString() : rawValue;
        if (address == null) {
            return "";
        }
        String lower = address.toLowerCase(Locale.ROOT);
        return lower.startsWith("mailto:") ? address.substring("mailto:".length()) : address;
    }
}
