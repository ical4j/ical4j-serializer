package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import net.fortuna.ical4j.model.Property;

import java.io.IOException;

/**
 * Implementations derive properties from JSON/XML constructs.
 */
public interface PropertyMapper {

    Property map(JsonParser parser) throws IOException;
}
