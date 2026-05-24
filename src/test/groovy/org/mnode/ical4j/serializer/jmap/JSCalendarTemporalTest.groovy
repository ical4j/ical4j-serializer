package org.mnode.ical4j.serializer.jmap

import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.property.DtStart
import spock.lang.Specification

import java.time.Instant

class JSCalendarTemporalTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    def 'UTC Instant becomes Etc/UTC zone with local timestamp'() {
        given:
        DtStart dtstart = builder.dtstart('20240516T090000Z')

        when:
        def t = JSCalendarTemporal.from(dtstart)

        then:
        t.localDateTime() == '2024-05-16T09:00:00'
        t.timeZone() == 'Etc/UTC'
        !t.showWithoutTime()
    }

    def 'Date-only value sets showWithoutTime and omits zone'() {
        given:
        DtStart dtstart = builder.dtstart('20240516', parameters: builder.parameters { value 'DATE' })

        when:
        def t = JSCalendarTemporal.from(dtstart)

        then:
        t.localDateTime() == '2024-05-16T00:00:00'
        t.timeZone() == null
        t.showWithoutTime()
    }

    def 'Floating local-time has no zone'() {
        given:
        DtStart dtstart = builder.dtstart('20240516T090000')

        when:
        def t = JSCalendarTemporal.from(dtstart)

        then:
        t.localDateTime() == '2024-05-16T09:00:00'
        t.timeZone() == null
        !t.showWithoutTime()
    }

    def 'Zoned datetime preserves source TZID'() {
        given:
        DtStart dtstart = builder.dtstart('20240516T090000', parameters: builder.parameters { tzid_ 'Australia/Sydney' })

        when:
        def t = JSCalendarTemporal.from(dtstart)

        then:
        t.localDateTime() == '2024-05-16T09:00:00'
        t.timeZone() == 'Australia/Sydney'
    }

    def 'formatUtc emits trailing Z'() {
        expect:
        JSCalendarTemporal.formatUtc(Instant.parse('2024-01-01T12:00:00Z')) == '2024-01-01T12:00:00Z'
    }
}
