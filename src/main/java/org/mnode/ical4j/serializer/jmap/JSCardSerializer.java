package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.property.Address;
import net.fortuna.ical4j.vcard.property.BDay;
import net.fortuna.ical4j.vcard.property.Email;
import net.fortuna.ical4j.vcard.property.Fn;
import net.fortuna.ical4j.vcard.property.N;
import net.fortuna.ical4j.vcard.property.Telephone;
import net.fortuna.ical4j.vcard.property.Url;

import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;

/**
 * Serializer for JSContact {@code Card} objects backed by an ical4j-vcard {@link Entity}.
 */
public class JSCardSerializer extends StdSerializer<Entity> {

    public JSCardSerializer() {
        super(Entity.class);
    }

    public JSCardSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildCard(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildCard(Entity card) throws ConstraintViolationException {
        return new JSCardBuilder().component(card).build();
    }

    public static class JSCardBuilder extends AbstractJSContactBuilder {

        public JSCardBuilder() {
            super("Card");
        }

        @Override
        public JsonNode build() {
            ObjectNode card = createObjectNode();
            applyUid(card);
            applyName(card);
            applyEmails(card);
            applyPhones(card);
            applyAddresses(card);
            applyAnniversaries(card);
            applyOnlineServices(card);
            return card;
        }

        private void applyUid(ObjectNode target) {
            component.getProperty("UID").ifPresent(uid ->
                    target.put("uid", ((net.fortuna.ical4j.model.Property) uid).getValue()));
        }

        private void applyName(ObjectNode target) {
            Optional<Fn> fn = component.getProperty("FN");
            Optional<N> n = component.getProperty("N");
            if (fn.isEmpty() && n.isEmpty()) {
                return;
            }
            ObjectNode name = target.objectNode();
            fn.ifPresent(value -> name.put("full", value.getValue()));
            n.ifPresent(value -> {
                if (value.getGivenName() != null && !value.getGivenName().isEmpty()) {
                    name.put("given", value.getGivenName());
                }
                if (value.getFamilyName() != null && !value.getFamilyName().isEmpty()) {
                    name.put("surname", value.getFamilyName());
                }
            });
            target.set("name", name);
        }

        private void applyEmails(ObjectNode target) {
            List<Email> emails = component.getProperties("EMAIL");
            if (emails.isEmpty()) {
                return;
            }
            ObjectNode container = target.objectNode();
            int index = 1;
            for (Email email : emails) {
                ObjectNode entry = target.objectNode();
                entry.put("@type", "EmailAddress");
                entry.put("address", email.getValue());
                container.set("email-" + index, entry);
                index++;
            }
            target.set("emails", container);
        }

        private void applyPhones(ObjectNode target) {
            List<Telephone> phones = component.getProperties("TEL");
            if (phones.isEmpty()) {
                return;
            }
            ObjectNode container = target.objectNode();
            int index = 1;
            for (Telephone phone : phones) {
                ObjectNode entry = target.objectNode();
                entry.put("@type", "Phone");
                entry.put("number", phone.getValue());
                container.set("phone-" + index, entry);
                index++;
            }
            target.set("phones", container);
        }

        private void applyAddresses(ObjectNode target) {
            List<Address> addresses = component.getProperties("ADR");
            if (addresses.isEmpty()) {
                return;
            }
            ObjectNode container = target.objectNode();
            int index = 1;
            for (Address address : addresses) {
                ObjectNode entry = target.objectNode();
                entry.put("@type", "Address");
                entry.put("full", address.getValue());
                container.set("address-" + index, entry);
                index++;
            }
            target.set("addresses", container);
        }

        private void applyAnniversaries(ObjectNode target) {
            Optional<BDay<Temporal>> bday = component.getProperty("BDAY");
            if (bday.isEmpty()) {
                return;
            }
            ObjectNode container = target.objectNode();
            ObjectNode entry = target.objectNode();
            entry.put("@type", "Anniversary");
            entry.put("kind", "birth");
            ObjectNode date = target.objectNode();
            date.put("@type", "Timestamp");
            date.put("utc", bday.get().getValue());
            entry.set("date", date);
            container.set("anniversary-1", entry);
            target.set("anniversaries", container);
        }

        private void applyOnlineServices(ObjectNode target) {
            List<Url> urls = component.getProperties("URL");
            if (urls.isEmpty()) {
                return;
            }
            ObjectNode container = target.objectNode();
            int index = 1;
            for (Url url : urls) {
                ObjectNode entry = target.objectNode();
                entry.put("@type", "Resource");
                entry.put("uri", url.getValue());
                container.set("url-" + index, entry);
                index++;
            }
            target.set("onlineServices", container);
        }
    }
}
