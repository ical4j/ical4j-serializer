package org.mnode.ical4j.serializer.jotn;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.VAvailability;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VJournal;
import net.fortuna.ical4j.model.component.VToDo;
import org.mnode.ical4j.serializer.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentListMapper extends StdDeserializer<ComponentList<? extends Component>> implements JsonMapper {

    private final ContentMapper<VEvent> vEventMapper;
    private final ContentMapper<VToDo> vToDoMapper;
    private final ContentMapper<VJournal> vJournalMapper;
    private final ContentMapper<VAvailability> vAvailabilityMapper;


    public ComponentListMapper() {
        this(null);
    }

    public ComponentListMapper(Class<?> vc) {
        super(vc);
        this.vEventMapper = new ContentMapper<>(VEvent::new);
        this.vToDoMapper = new ContentMapper<>(VToDo::new);
        this.vJournalMapper = new ContentMapper<>(VJournal::new);
        this.vAvailabilityMapper = new ContentMapper<>(VAvailability::new);
    }

    @Override
    public ComponentList<? extends Component> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        List<Component> components = new ArrayList<>();

        while (JsonToken.FIELD_NAME.equals(p.nextToken())) {
            var componentType = p.currentName();
            assertNextToken(p, JsonToken.START_OBJECT);
            switch (componentType) {
                case "event":
                    components.add(vEventMapper.deserialize(p, ctxt));
                    break;
                case "to-do":
                    components.add(vToDoMapper.deserialize(p, ctxt));
                    break;
                case "journal":
                    components.add(vJournalMapper.deserialize(p, ctxt));
                    break;
                case "availability":
                    components.add(vAvailabilityMapper.deserialize(p, ctxt));
                    break;
            }
        }

        return new ComponentList<>(components);
    }
}
