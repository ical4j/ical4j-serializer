package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Uid;
import org.mnode.ical4j.json.jot.JournalBuilder;

import java.io.IOException;

public class JotToDoSerializer extends StdSerializer<VToDo> {

    public JotToDoSerializer(Class<VToDo> t) {
        super(t);
    }

    @Override
    public void serialize(VToDo value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeTree(buildTodo(value));
    }

    private JsonNode buildTodo(VToDo toDo) {
        Uid uid = toDo.getProperty(Property.UID);
        JournalBuilder builder = new JournalBuilder().uid(uid);

        return builder.build();
    }
}
