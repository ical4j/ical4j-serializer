package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import net.fortuna.ical4j.model.Parameter;
import net.fortuna.ical4j.model.ParameterBuilder;
import net.fortuna.ical4j.model.ParameterFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParameterMapperImpl implements ParameterMapper {

    private final List<ParameterFactory<?>> parameterFactories;

    public ParameterMapperImpl(List<ParameterFactory<?>> parameterFactories) {
        this.parameterFactories = parameterFactories;
    }

    /**
     * Build a parameter from the given JSON parser.
     * @param p a JSON parser with a pointer to an expected parameter representation
     * @return a parameter constructed from the given JSON parser
     * @throws IOException
     */
    @Override
    public Parameter map(JsonParser p) throws IOException {
        if (Arrays.asList(JsonToken.VALUE_FALSE, JsonToken.VALUE_TRUE).contains(p.nextToken())) {
            return new ParameterBuilder(parameterFactories).name(p.currentName())
                    .value(p.currentToken().asString()).build();
        } else {
            return new ParameterBuilder(parameterFactories).name(p.currentName())
                    .value(p.getText()).build();
        }
    }
}
