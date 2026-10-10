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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        /**
         * A vCard date-time: a full or truncated date, a time of hour[minute[second]], and an optional offset.
         */
        private static final Pattern DATE_TIME = Pattern.compile(
                "(\\d{8}|--\\d{4}|---\\d{2})T(\\d{2})(\\d{2})?(\\d{2})?(Z|[+-]\\d{2}(?:\\d{2})?)?");

        /** A full (YYYYMMDD) or reduced (YYYY-MM, YYYY) date. */
        private static final Pattern FULL_DATE = Pattern.compile("(\\d{4})(?:-?(\\d{2})(?:-?(\\d{2}))?)?");

        /** A truncated date without year (--MMDD, --MM). */
        private static final Pattern NO_YEAR = Pattern.compile("--(\\d{2})(\\d{2})?");

        /** A truncated date with only the day (---DD). */
        private static final Pattern DAY_ONLY = Pattern.compile("---(\\d{2})");

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
            ObjectNode date = anniversaryDate(bday.get().getValue(), target);
            if (date == null) {
                // free text (e.g. VALUE=text) has no JSContact equivalent..
                return;
            }
            ObjectNode container = target.objectNode();
            ObjectNode entry = target.objectNode();
            entry.put("@type", "Anniversary");
            entry.put("kind", "birth");
            entry.set("date", date);
            container.set("anniversary-1", entry);
            target.set("anniversaries", container);
        }

        /**
         * Map a vCard date or date-time value (RFC 6350, 4.3) to a JSContact anniversary date. Dates, including
         * reduced and truncated forms such as "1980" or "--0415", become a PartialDate. Date-times with a UTC
         * offset become a UTC Timestamp, and local date-times keep only their date part.
         * @return the date node, or null if the value is not a recognised date format
         */
        private ObjectNode anniversaryDate(String value, ObjectNode parent) {
            if (value == null) {
                return null;
            }
            Matcher dateTime = DATE_TIME.matcher(value);
            if (dateTime.matches() && dateTime.group(5) != null && dateTime.group(1).length() == 8) {
                LocalDateTime local = LocalDateTime.of(LocalDate.parse(dateTime.group(1), DateTimeFormatter.BASIC_ISO_DATE),
                        LocalTime.of(Integer.parseInt(dateTime.group(2)),
                                dateTime.group(3) != null ? Integer.parseInt(dateTime.group(3)) : 0,
                                dateTime.group(4) != null ? Integer.parseInt(dateTime.group(4)) : 0));
                ZoneOffset offset = "Z".equals(dateTime.group(5)) ? ZoneOffset.UTC : ZoneOffset.of(dateTime.group(5));
                ObjectNode node = parent.objectNode();
                node.put("@type", "Timestamp");
                node.put("utc", local.atOffset(offset).toInstant().toString());
                return node;
            }
            String date = dateTime.matches() ? dateTime.group(1) : value;
            Integer year = null;
            Integer month = null;
            Integer day = null;
            Matcher m;
            if ((m = FULL_DATE.matcher(date)).matches()) {
                year = Integer.valueOf(m.group(1));
                month = m.group(2) != null ? Integer.valueOf(m.group(2)) : null;
                day = m.group(3) != null ? Integer.valueOf(m.group(3)) : null;
            } else if ((m = NO_YEAR.matcher(date)).matches()) {
                month = Integer.valueOf(m.group(1));
                day = m.group(2) != null ? Integer.valueOf(m.group(2)) : null;
            } else if ((m = DAY_ONLY.matcher(date)).matches()) {
                day = Integer.valueOf(m.group(1));
            } else {
                return null;
            }
            ObjectNode node = parent.objectNode();
            node.put("@type", "PartialDate");
            if (year != null) {
                node.put("year", year);
            }
            if (month != null) {
                node.put("month", month);
            }
            if (day != null) {
                node.put("day", day);
            }
            return node;
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
