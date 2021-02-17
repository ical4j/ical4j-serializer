package org.mnode.ical4j.json

import spock.lang.Specification

class JSEventBuilderTest extends Specification {

    def 'assert build functionality'() {
        given: 'a builder'
        JSEventBuilder builder = []

        expect:
        builder.build().toString() == '{"@type":"jsevent"}'
    }
}
