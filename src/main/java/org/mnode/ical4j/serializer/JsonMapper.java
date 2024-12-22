package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.time.format.DateTimeParseException;

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
