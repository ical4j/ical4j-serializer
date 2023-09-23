package org.mnode.ical4j.serializer.model;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.model.Component;
import org.mnode.ical4j.serializer.JsonMapper;
import org.mnode.ical4j.serializer.model.component.VAvailabilityMapper;
import org.mnode.ical4j.serializer.model.component.VEventMapper;
import org.mnode.ical4j.serializer.model.component.VJournalMapper;
import org.mnode.ical4j.serializer.model.component.VToDoMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComponentListMapper extends StdDeserializer<List<? extends Component>> implements JsonMapper {

    private final VEventMapper vEventMapper;
    private final VToDoMapper vToDoMapper;
    private final VJournalMapper vJournalMapper;
    private final VAvailabilityMapper vAvailabilityMapper;


    public ComponentListMapper() {
        this(null);
    }

    public ComponentListMapper(Class<?> vc) {
        super(vc);
        this.vEventMapper = new VEventMapper(null);
        this.vToDoMapper = new VToDoMapper(null);
        this.vJournalMapper = new VJournalMapper(null);
        this.vAvailabilityMapper = new VAvailabilityMapper(null);
    }

    @Override
    public List<? extends Component> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        List<Component> components = new ArrayList<>();

        while (JsonToken.FIELD_NAME.equals(p.nextToken())) {
            String componentType = p.currentName();
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

        return components;
    }
}
