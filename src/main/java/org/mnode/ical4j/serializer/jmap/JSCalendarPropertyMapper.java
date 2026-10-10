package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Created;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.Sequence;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Url;

import java.util.Locale;
import java.util.Optional;

/**
 * Static helpers that map common iCalendar scalar properties onto JSCalendar
 * fields. Each helper is a no-op when the source property is absent.
 */
final class JSCalendarPropertyMapper {

    private JSCalendarPropertyMapper() {
    }

    static void applyUid(PropertyContainer source, ObjectNode target) {
        Optional<Uid> uid = source.getProperty("UID");
        uid.ifPresent(value -> target.put("uid", value.getValue()));
    }

    static void applyTitle(PropertyContainer source, ObjectNode target) {
        Optional<Summary> summary = source.getProperty("SUMMARY");
        summary.ifPresent(value -> target.put("title", value.getValue()));
    }

    static void applyDescription(PropertyContainer source, ObjectNode target) {
        Optional<Description> description = source.getProperty("DESCRIPTION");
        description.ifPresent(value -> target.put("description", value.getValue()));
    }

    static void applyTimestamps(PropertyContainer source, ObjectNode target) {
        Optional<Created> created = source.getProperty("CREATED");
        created.ifPresent(value -> target.put("created", JSCalendarTemporal.formatUtc(value.getDate())));

        Optional<LastModified> updated = source.getProperty("LAST-MODIFIED");
        updated.ifPresent(value -> target.put("updated", JSCalendarTemporal.formatUtc(value.getDate())));
    }

    static void applySequence(PropertyContainer source, ObjectNode target) {
        Optional<Sequence> sequence = source.getProperty("SEQUENCE");
        sequence.ifPresent(value -> target.put("sequence", value.getSequenceNo()));
    }

    static void applyPriority(PropertyContainer source, ObjectNode target) {
        Optional<Priority> priority = source.getProperty("PRIORITY");
        priority.ifPresent(value -> target.put("priority", value.getLevel()));
    }

    /**
     * Map an event STATUS to the JSCalendar {@code status} property, which only supports
     * confirmed, tentative and cancelled. Other values are omitted.
     */
    static void applyStatus(PropertyContainer source, ObjectNode target) {
        Optional<Status> status = source.getProperty("STATUS");
        status.map(value -> mapStatus(value.getValue())).ifPresent(value -> target.put("status", value));
    }

    /**
     * Map a task STATUS to the JSCalendar {@code progress} property, which supports
     * needs-action, in-process, completed and cancelled. Other values are omitted.
     */
    static void applyProgress(PropertyContainer source, ObjectNode target) {
        Optional<Status> status = source.getProperty("STATUS");
        status.map(value -> mapProgress(value.getValue())).ifPresent(value -> target.put("progress", value));
    }

    static void applyCategoriesAsKeywords(PropertyContainer source, ObjectNode target) {
        var categories = source.<Categories>getProperties("CATEGORIES");
        if (categories.isEmpty()) {
            return;
        }
        ObjectNode keywords = target.objectNode();
        for (Categories prop : categories) {
            for (var category : prop.getCategories().getTexts()) {
                keywords.put(category, true);
            }
        }
        if (!keywords.isEmpty()) {
            target.set("keywords", keywords);
        }
    }

    static void applyColor(PropertyContainer source, ObjectNode target) {
        Optional<net.fortuna.ical4j.model.property.Color> color = source.getProperty("COLOR");
        color.ifPresent(value -> target.put("color", value.getValue()));
    }

    /**
     * Map CLASS to the JSCalendar {@code privacy} property. CONFIDENTIAL has no direct equivalent
     * and maps to secret, the most restrictive JSCalendar value. Other values are omitted.
     */
    static void applyPrivacy(PropertyContainer source, ObjectNode target) {
        Optional<Clazz> privacy = source.getProperty("CLASS");
        privacy.map(value -> mapPrivacy(value.getValue())).ifPresent(value -> target.put("privacy", value));
    }

    static void applyLinkFromUrl(PropertyContainer source, ObjectNode target) {
        Optional<Url> url = source.getProperty("URL");
        url.ifPresent(value -> {
            ObjectNode link = target.objectNode();
            link.put("@type", "Link");
            link.put("href", value.getValue());
            link.put("rel", "about");
            ObjectNode links = target.objectNode();
            links.set("url", link);
            target.set("links", links);
        });
    }

    private static String mapStatus(String raw) {
        if (raw == null) {
            return null;
        }
        switch (raw.toUpperCase(Locale.ROOT)) {
            case "CONFIRMED":
                return "confirmed";
            case "TENTATIVE":
                return "tentative";
            case "CANCELLED":
                return "cancelled";
            default:
                return null;
        }
    }

    private static String mapProgress(String raw) {
        if (raw == null) {
            return null;
        }
        switch (raw.toUpperCase(Locale.ROOT)) {
            case "NEEDS-ACTION":
                return "needs-action";
            case "IN-PROCESS":
                return "in-process";
            case "COMPLETED":
                return "completed";
            case "CANCELLED":
                return "cancelled";
            default:
                return null;
        }
    }

    private static String mapPrivacy(String raw) {
        if (raw == null) {
            return null;
        }
        switch (raw.toUpperCase(Locale.ROOT)) {
            case "PUBLIC":
                return "public";
            case "PRIVATE":
                return "private";
            case "CONFIDENTIAL":
                return "secret";
            default:
                return null;
        }
    }
}
