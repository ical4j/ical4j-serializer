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

    def 'date-only DTSTART/DTEND pair becomes a duration in days'() {
        given:
        DtStart start = builder.dtstart('20240516', parameters: builder.parameters { value 'DATE' })
        DtEnd end = builder.dtend('20240517', parameters: builder.parameters { value 'DATE' })

        expect:
        JSCalendarDuration.between(start, end) == 'P1D'
    }

    def 'negative amounts are formatted with a single leading sign'() {
        expect:
        JSCalendarDuration.format(amount) == expected

        where:
        amount                                  | expected
        java.time.Duration.ofMinutes(-15)       | '-PT15M'
        java.time.Duration.ofMinutes(15)        | 'PT15M'
        java.time.Period.ofDays(-1)             | '-P1D'
        java.time.Period.ofWeeks(1)             | 'P7D'
    }
}
