package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Organizer;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Builds the JSCalendar {@code participants} map from a component's
 * {@code ORGANIZER} and {@code ATTENDEE} properties.
 */
final class ParticipantBuilder {

    private static final String MAILTO = "mailto:";

    private ParticipantBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        Optional<Organizer> organizer = source.getProperty("ORGANIZER");
        List<Attendee> attendees = source.getProperties("ATTENDEE");
        if (organizer.isEmpty() && attendees.isEmpty()) {
            return;
        }
        Map<String, ObjectNode> participants = new LinkedHashMap<>();
        organizer.ifPresent(value -> {
            ObjectNode roles = participant(value.getCalAddress(), value.getValue(), participants, target);
            roles.put("owner", true);
        });
        for (Attendee attendee : attendees) {
            ObjectNode roles = participant(attendee.getCalAddress(), attendee.getValue(), participants, target);
            applyAttendeeRoles(attendee, roles);
        }
        ObjectNode node = target.objectNode();
        participants.forEach(node::set);
        target.set("participants", node);
    }

    /**
     * Find or create the participant for an address, merging ORGANIZER and ATTENDEE entries that refer to the
     * same calendar user.
     * @return the participant's roles object
     */
    private static ObjectNode participant(URI calAddress, String rawValue, Map<String, ObjectNode> participants,
                                          ObjectNode parent) {
        String address = normalizeAddress(calAddress, rawValue);
        ObjectNode participant = participants.computeIfAbsent(participantId(address), k -> {
            ObjectNode node = parent.objectNode();
            node.put("@type", "Participant");
            ObjectNode sendTo = parent.objectNode();
            // only mailto addresses can be reached via iMIP..
            sendTo.put(address.startsWith(MAILTO) ? "imip" : "other", address);
            node.set("sendTo", sendTo);
            node.set("roles", parent.objectNode());
            return node;
        });
        return (ObjectNode) participant.get("roles");
    }

    /**
     * Map the iCalendar ROLE parameter to JSCalendar roles. ROLE defaults to REQ-PARTICIPANT.
     */
    private static void applyAttendeeRoles(Property attendee, ObjectNode roles) {
        String role = attendee.<Role>getParameter(Parameter.ROLE).map(Role::getValue)
                .orElse(Role.REQ_PARTICIPANT.getValue()).toUpperCase(Locale.ROOT);
        switch (role) {
            case "CHAIR":
                roles.put("attendee", true);
                roles.put("chair", true);
                break;
            case "OPT-PARTICIPANT":
                roles.put("attendee", true);
                roles.put("optional", true);
                break;
            case "NON-PARTICIPANT":
                roles.put("informational", true);
                break;
            default:
                roles.put("attendee", true);
        }
    }

    /**
     * Normalize a calendar user address so that differently-cased forms of the same address are merged.
     * Addresses without a scheme are assumed to be email addresses.
     */
    private static String normalizeAddress(URI calAddress, String rawValue) {
        String address = calAddress != null ? calAddress.toString() : rawValue;
        if (address == null) {
            return "";
        }
        if (address.regionMatches(true, 0, MAILTO, 0, MAILTO.length())) {
            return MAILTO + address.substring(MAILTO.length()).toLowerCase(Locale.ROOT);
        }
        if (address.indexOf(':') < 0 && address.indexOf('@') > 0) {
            return MAILTO + address.toLowerCase(Locale.ROOT);
        }
        return address;
    }

    /**
     * Derive a stable participant id. JSCalendar ids are limited to the URL-safe base64 alphabet, so an
     * address cannot be used directly; a name-based UUID keeps ids valid and deterministic.
     */
    private static String participantId(String address) {
        return UUID.nameUUIDFromBytes(address.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
