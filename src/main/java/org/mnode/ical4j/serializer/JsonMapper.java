package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterBuilder;
import net.fortuna.ical4j.model.ParameterFactory;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Support for deserialization of JSON encoded object representations.
 */
public interface JsonMapper {

    default void assertNextToken(JsonParser p, JsonToken token) throws IOException {
        if (!token.equals(p.nextToken())) {
            throw new IllegalArgumentException(String.format("Invalid input: %s", p.currentToken()));
        }
    }

    default void assertCurrentToken(JsonParser p, JsonToken token) {
        if (!token.equals(p.currentToken())) {
            throw new IllegalArgumentException(String.format("Invalid input: %s", p.currentToken()));
        }
    }

    default void assertNextScalarValue(JsonParser p) throws IOException {
        if (!p.nextToken().isScalarValue()) {
            throw new IllegalArgumentException("Invalid input: non-scalar value");
        }
    }

    default void assertCurrentScalarValue(JsonParser p) throws IOException {
        if (!p.currentToken().isScalarValue()) {
            throw new IllegalArgumentException("Invalid input: non-scalar value");
        }
    }

    default void assertTextValue(JsonParser p, String value) throws IOException {
        if (!value.equals(p.nextTextValue())) {
            throw new IllegalArgumentException(String.format("Invalid input: %s", p.currentValue()));
        }
    }

    default void assertNextName(JsonParser p, String value) throws IOException {
        if (!value.equals(p.nextFieldName())) {
            throw new IllegalArgumentException(String.format("Invalid input: %s", p.currentName()));
        }
    }

    /**
     * Build a parameter from the given JSON parser.
     * @param p a JSON parser with a pointer to an expected parameter representation
     * @param parameterFactories a list of factories used to construct supported parameters
     * @return a parameter constructed from the given JSON parser
     * @throws IOException
     */
    default Parameter parseParameter(JsonParser p, List<ParameterFactory<?>> parameterFactories) throws IOException {
        if (Arrays.asList(JsonToken.VALUE_FALSE, JsonToken.VALUE_TRUE).contains(p.nextToken())) {
            return new ParameterBuilder(parameterFactories).name(p.currentName())
                    .value(p.currentToken().asString()).build();
        } else {
            return new ParameterBuilder(parameterFactories).name(p.currentName())
                    .value(p.getText()).build();
        }
    }

    default String decodeValue(String propertyName, String value) {
        switch (propertyName) {
            case "trigger":
            case "created":
            case "last-modified":
            case "recurrence-id":
            case "dtstamp":
                return JCalDecoder.INSTANT.decode(value);
            case "dtstart":
            case "dtend":
            case "rdate":
            case "exdate":
            case "due":
                try {
                    return JCalDecoder.DATE_TIME.decode(value);
                } catch (DateTimeParseException e) {
                    return JCalDecoder.DATE.decode(value);
                }
            default: return value;
        }
    }

}
