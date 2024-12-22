package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import net.fortuna.ical4j.model.Parameter;

import java.io.IOException;

/**
 * Implementations derive parameters from JSON/XML constructs.
 */
public interface ParameterMapper {

    Parameter map(JsonParser parser) throws IOException;
}
