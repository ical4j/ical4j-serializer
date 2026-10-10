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
        alert.get('trigger').get('offset').asText() == '-PT15M'
        !alert.get('trigger').has('relativeTo')
    }

    def 'trigger related to end sets relativeTo'() {
        given:
        VEvent event = builder.vevent {
            uid '1'
            valarm {
                action 'DISPLAY'
                trigger '-PT5M', parameters: parameters { related 'END' }
            }
        }

        when:
        AlertBuilder.applyTo(event, target)
        def trigger = target.get('alerts').get('alert-1').get('trigger')

        then:
        trigger.get('offset').asText() == '-PT5M'
        trigger.get('relativeTo').asText() == 'end'
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
