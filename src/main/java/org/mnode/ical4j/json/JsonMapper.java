package org.mnode.ical4j.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public interface JsonMapper {

    default void assertNextToken(JsonParser p, JsonToken token) throws IOException {
        if (!token.equals(p.nextToken())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    default void assertCurrentToken(JsonParser p, JsonToken token) {
        if (!token.equals(p.currentToken())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    default void assertTextValue(JsonParser p, String value) throws IOException {
        if (!value.equals(p.nextTextValue())) {
            throw new IllegalArgumentException("Invalid input");
        }
    }

}
