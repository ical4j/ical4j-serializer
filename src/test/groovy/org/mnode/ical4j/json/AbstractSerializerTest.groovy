package org.mnode.ical4j.json

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.Attach
import net.fortuna.ical4j.util.Calendars
import net.fortuna.ical4j.vcard.VCard
import spock.lang.Shared
import spock.lang.Specification

class AbstractSerializerTest extends Specification {

    @Shared
    Calendar calendar1, calendar2

    @Shared
    VEvent event1, event2

    @Shared
    VCard card1, card2, card3

    def setupSpec() {
        ContentBuilder builder = []
        event1 = builder.vevent() {
            uid '1'
//            dtstamp()
            summary 'Test Event 1'
            dtstart '20090810', parameters: parameters { value 'DATE' }
            action 'DISPLAY'
            attach new Attach(new File('LICENSE').bytes)
        }

        event2 = builder.vevent() {
            uid '2'
            summary 'Test Event 2'
            dtstart '20100810', parameters: parameters { value 'DATE' }
        }

        calendar1 = builder.calendar() {
            prodid '-//Ben Fortuna//iCal4j 1.0//EN'
            version '2.0'
            uid '123'
            vevent(event1)
            vevent(event2)
        }

        calendar2 = Calendars.load('src/test/resources/samples/justin.ics')

        net.fortuna.ical4j.vcard.ContentBuilder vcardBuilder = []
        card1 = vcardBuilder.vcard() {
            fn 'Test Card'
            uid '1'
        }

        card2 = vcardBuilder.vcard() {
            fn 'Jane Doe'
            uid '2'
            adr ';;20341 Whitworth Institute 405 N. Whitworth;Seattle;WA;98052'
        }

        card3 = vcardBuilder.vcard() {
            fn 'Acme Inc.'
            uid '3'
            logo 'http://example.org/acme-logo.png'
            adr ';;20341 Whitworth Institute 405 N. Whitworth;Seattle;WA;98052'
        }
    }
}
