package org.mnode.ical4j.json.jscalendar

import spock.lang.Specification

class JSGroupBuilderTest extends Specification {

    def 'assert build functionality'() {
        given: 'a builder'
        JSGroupBuilder builder = []

        expect:
        builder.uid("1").build().toString() == '{"@type":"jsgroup","uid":"1"}'
    }

    def 'assert mandatory fields'() {
        given: 'a builder'
        JSGroupBuilder builder = []

        when: 'built without mandatory fields'
        builder.build().toString()

        then:
        thrown(IllegalArgumentException)
    }
}
