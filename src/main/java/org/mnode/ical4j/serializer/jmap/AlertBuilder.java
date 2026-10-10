package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.ComponentContainer;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.parameter.Related;
import net.fortuna.ical4j.model.property.Action;
import net.fortuna.ical4j.model.property.Trigger;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Builds the JSCalendar {@code alerts} map from a component's nested
 * {@code VALARM} sub-components.
 */
final class AlertBuilder {

    private AlertBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        if (!(source instanceof ComponentContainer)) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<?> components = ((ComponentContainer<?>) source).getComponents("VALARM");
        if (components.isEmpty()) {
            return;
        }
        ObjectNode container = target.objectNode();
        int index = 1;
        for (Object component : components) {
            if (!(component instanceof VAlarm)) {
                continue;
            }
            VAlarm alarm = (VAlarm) component;
            ObjectNode node = target.objectNode();
            node.put("@type", "Alert");
            Optional<Action> action = alarm.getProperty("ACTION");
            action.ifPresent(value -> node.put("action", value.getValue().toLowerCase(Locale.ROOT)));
            Optional<Trigger> trigger = alarm.getProperty("TRIGGER");
            trigger.ifPresent(value -> node.set("trigger", buildTrigger(value, target)));
            container.set("alert-" + index, node);
            index++;
        }
        if (!container.isEmpty()) {
            target.set("alerts", container);
        }
    }

    private static ObjectNode buildTrigger(Trigger trigger, ObjectNode parent) {
        ObjectNode node = parent.objectNode();
        if (trigger.isAbsolute()) {
            node.put("@type", "AbsoluteTrigger");
            node.put("when", JSCalendarTemporal.formatUtc(trigger.getDate()));
        } else {
            node.put("@type", "OffsetTrigger");
            node.put("offset", JSCalendarDuration.format(trigger.getDuration()));
            Optional<Related> related = trigger.getParameter(Parameter.RELATED);
            if (related.isPresent() && Related.END.equals(related.get())) {
                node.put("relativeTo", "end");
            }
        }
        return node;
    }
}
