package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import net.fortuna.ical4j.model.Component;

import java.io.IOException;

/**
 * Implementations derive components from JSON/XML constructs.
 */
public interface ComponentMapper {

    Component map(JsonParser parser) throws IOException;
}
