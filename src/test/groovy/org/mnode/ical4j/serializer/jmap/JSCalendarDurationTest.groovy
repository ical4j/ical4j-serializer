package org.mnode.ical4j.serializer.jmap

import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.property.DtEnd
import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.Duration
import spock.lang.Specification

class JSCalendarDurationTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    def 'Duration property serializes to ISO-8601 string'() {
        given:
        Duration duration = builder.duration('PT1H30M')

        expect:
        JSCalendarDuration.from(duration) == 'PT1H30M'
    }

    def 'DTSTART/DTEND pair becomes computed duration'() {
        given:
        DtStart start = builder.dtstart('20240516T090000Z')
        DtEnd end = builder.dtend('20240516T103000Z')

        expect:
        JSCalendarDuration.between(start, end) == 'PT1H30M'
    }
}
