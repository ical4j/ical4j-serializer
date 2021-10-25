package org.mnode.ical4j.json

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.*
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
    VFreeBusy freebusy1

    @Shared
    VJournal journal1

    @Shared
    VToDo todo1

    @Shared
    VAvailability availability1

    @Shared
    VAlarm alarm1

    @Shared
    Available available1

    @Shared
    VCard card1, card2, card3

    def setupSpec() {
        ContentBuilder builder = []
        event1 = builder.vevent() {
            uid '1'
//            dtstamp()
            summary 'Test Event 1'
            organizer 'johnd@example.com'
            dtstart '20090810', parameters: parameters { value 'DATE' }
            action 'DISPLAY'
            attach new Attach(new File('LICENSE').bytes)
        }

        event2 = builder.vevent() {
            uid '2'
            summary 'Test Event 2'
            organizer 'johnd@example.com', parameters: parameters { cn 'John Doe' }
            dtstart '20100810', parameters: parameters { value 'DATE' }
            description 'Test Description 2', parameters: parameters { xparameter name: 'x-format', value: 'text/plain' }
            xproperty 'x-test', value: 'test-value'
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

        freebusy1 = builder.vfreebusy() { uid '123'}
        journal1 = builder.vjournal() { uid '123'}
        todo1 = builder.vtodo() { uid '123'}
        availability1 = builder.vavailability() { uid '123'}
        alarm1 = builder.valarm() { uid '123'}
        available1 = builder.available() { uid '123'}
    }
}
