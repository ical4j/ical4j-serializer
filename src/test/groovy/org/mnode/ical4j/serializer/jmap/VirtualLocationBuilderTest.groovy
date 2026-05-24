package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class VirtualLocationBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    def 'CONFERENCE produces a VirtualLocation entry'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            conference 'https://meet.example.com/room/1'
        }

        when:
        VirtualLocationBuilder.applyTo(event, target)
        def virt = target.get('virtualLocations').get('conference-1')

        then:
        virt.get('@type').asText() == 'VirtualLocation'
        virt.get('uri').asText() == 'https://meet.example.com/room/1'
    }

    def 'no virtualLocations property when source is empty'() {
        given:
        VEvent event = builder.vevent { uid '1' }

        when:
        VirtualLocationBuilder.applyTo(event, target)

        then:
        !target.has('virtualLocations')
    }
}
