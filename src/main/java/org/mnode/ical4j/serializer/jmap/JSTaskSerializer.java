package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.PercentComplete;

import java.io.IOException;
import java.util.Optional;

/**
 * Serializer for JMAP JSTask objects, which represent tasks in a calendar.
 * This class extends StdSerializer to provide custom serialization logic for VToDo components.
 */
public class JSTaskSerializer extends StdSerializer<VToDo> {

    public JSTaskSerializer() {
        super(VToDo.class);
    }

    public JSTaskSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildJSTask(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildJSTask(VToDo toDo) throws ConstraintViolationException {
        return new JSTaskBuilder().component(toDo).build();
    }

    public static class JSTaskBuilder extends AbstractJSCalendarBuilder<VToDo> {

        public JSTaskBuilder() {
            super("jstask");
        }

        @Override
        public JsonNode build() {
            ObjectNode jsTask = createObjectNode();
            JSCalendarPropertyMapper.applyUid(component, jsTask);
            JSCalendarPropertyMapper.applyTitle(component, jsTask);
            JSCalendarPropertyMapper.applyDescription(component, jsTask);
            JSCalendarPropertyMapper.applyTimestamps(component, jsTask);
            JSCalendarPropertyMapper.applySequence(component, jsTask);
            JSCalendarPropertyMapper.applyPriority(component, jsTask);
            JSCalendarPropertyMapper.applyStatus(component, jsTask);
            JSCalendarPropertyMapper.applyCategoriesAsKeywords(component, jsTask);
            JSCalendarPropertyMapper.applyColor(component, jsTask);
            JSCalendarPropertyMapper.applyPrivacy(component, jsTask);
            JSCalendarPropertyMapper.applyLinkFromUrl(component, jsTask);
            applyDueAndStart(component, jsTask);
            applyPercentComplete(jsTask);
            ParticipantBuilder.applyTo(component, jsTask);
            LocationBuilder.applyTo(component, jsTask);
            VirtualLocationBuilder.applyTo(component, jsTask);
            AlertBuilder.applyTo(component, jsTask);
            RecurrenceRuleBuilder.applyTo(component, jsTask);
            return jsTask;
        }

        private void applyPercentComplete(ObjectNode target) {
            Optional<PercentComplete> percent = component.getProperty("PERCENT-COMPLETE");
            percent.ifPresent(value -> target.put("percentComplete", value.getPercentage()));
        }
    }
}
