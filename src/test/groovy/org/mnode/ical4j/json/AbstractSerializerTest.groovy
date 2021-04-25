package org.mnode.ical4j.json

import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.ContentBuilder
import spock.lang.Shared
import spock.lang.Specification

class AbstractSerializerTest extends Specification {

    @Shared
    Calendar calendar1

    def setupSpec() {
        ContentBuilder builder = []
        calendar1 = builder.calendar() {
            prodid '-//Ben Fortuna//iCal4j 1.0//EN'
            version '2.0'
            uid '123'
            vevent {
                uid '1'
                dtstart '20090810', parameters: parameters { value 'DATE' }
            }
            vevent {
                uid '2'
                dtstart '20100810', parameters: parameters { value 'DATE' }
            }
        }
    }
}
