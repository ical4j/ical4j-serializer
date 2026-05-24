package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification

class AlertBuilderTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode target = new ObjectMapper().createObjectNode()

    def 'duration trigger becomes OffsetTrigger'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            valarm {
                action 'DISPLAY'
                trigger '-PT15M'
            }
        }

        when:
        AlertBuilder.applyTo(event, target)
        def alert = target.get('alerts').get('alert-1')

        then:
        alert.get('@type').asText() == 'Alert'
        alert.get('action').asText() == 'display'
        alert.get('trigger').get('@type').asText() == 'OffsetTrigger'
        alert.get('trigger').get('offset').asText() == 'PT-15M'
    }

    def 'no alerts property when no VALARM present'() {
        given:
        VEvent event = builder.vevent { uid '1' }

        when:
        AlertBuilder.applyTo(event, target)

        then:
        !target.has('alerts')
    }
}
