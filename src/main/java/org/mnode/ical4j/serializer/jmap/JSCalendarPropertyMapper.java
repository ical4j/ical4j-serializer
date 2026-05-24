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

    static void applyStatus(PropertyContainer source, ObjectNode target) {
        Optional<Status> status = source.getProperty("STATUS");
        status.ifPresent(value -> {
            String mapped = mapStatus(value.getValue());
            if (mapped != null) {
                target.put("status", mapped);
            }
        });
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

    static void applyPrivacy(PropertyContainer source, ObjectNode target) {
        Optional<Clazz> privacy = source.getProperty("CLASS");
        privacy.ifPresent(value -> {
            String raw = value.getValue();
            if (raw != null && !raw.isEmpty()) {
                target.put("privacy", raw.toLowerCase(Locale.ROOT));
            }
        });
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
            case "IN-PROCESS":
                return "in-process";
            case "COMPLETED":
                return "completed";
            case "NEEDS-ACTION":
                return "needs-action";
            default:
                return raw.toLowerCase(Locale.ROOT);
        }
    }
}
