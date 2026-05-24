package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class LocationBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    def 'LOCATION produces a named Location entry'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            location 'Conference Room A'
        }

        when:
        LocationBuilder.applyTo(event, target)
        def location = target.get('locations').get('location-1')

        then:
        location.get('@type').asText() == 'Location'
        location.get('name').asText() == 'Conference Room A'
    }

    def 'GEO without LOCATION still produces a Location with coordinates'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            geo '-33.8688;151.2093'
        }

        when:
        LocationBuilder.applyTo(event, target)
        def location = target.get('locations').get('location-1')

        then:
        location.get('coordinates').asText() == 'geo:-33.8688,151.2093'
    }

    def 'LOCATION + GEO merge into one entry'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            location 'Opera House'
            geo '-33.8568;151.2153'
        }

        when:
        LocationBuilder.applyTo(event, target)
        def location = target.get('locations').get('location-1')

        then:
        location.get('name').asText() == 'Opera House'
        location.get('coordinates').asText() == 'geo:-33.8568,151.2153'
    }

    def 'no locations property when source is empty'() {
        given:
        VEvent event = builder.vevent { uid '1' }

        when:
        LocationBuilder.applyTo(event, target)

        then:
        !target.has('locations')
    }
}
