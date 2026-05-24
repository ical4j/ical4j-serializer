package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.vcard.Entity;
import net.fortuna.ical4j.vcard.property.Fn;
import net.fortuna.ical4j.vcard.property.Member;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Serializer for JSContact {@code CardGroup} objects backed by a vCard
 * {@link Entity} with {@code KIND:group} and {@code MEMBER} properties.
 */
public class JSCardGroupSerializer extends StdSerializer<Entity> {

    public JSCardGroupSerializer() {
        super(Entity.class);
    }

    public JSCardGroupSerializer(Class<Entity> t) {
        super(t);
    }

    @Override
    public void serialize(Entity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        try {
            gen.writeTree(buildCardGroup(value));
        } catch (ConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode buildCardGroup(Entity entity) throws ConstraintViolationException {
        return new JSCardGroupBuilder().component(entity).build();
    }

    public static class JSCardGroupBuilder extends AbstractJSContactBuilder {

        public JSCardGroupBuilder() {
            super("CardGroup");
        }

        @Override
        public JsonNode build() {
            ObjectNode group = createObjectNode();
            component.getProperty("UID").ifPresent(uid ->
                    group.put("uid", ((net.fortuna.ical4j.model.Property) uid).getValue()));

            Optional<Fn> fn = component.getProperty("FN");
            fn.ifPresent(value -> {
                ObjectNode name = group.objectNode();
                name.put("full", value.getValue());
                group.set("name", name);
            });

            List<Member> members = component.getProperties("MEMBER");
            if (!members.isEmpty()) {
                ObjectNode container = group.objectNode();
                for (Member member : members) {
                    container.put(member.getValue(), true);
                }
                group.set("members", container);
            }
            return group;
        }
    }
}
