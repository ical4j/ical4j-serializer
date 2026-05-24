package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.vcard.ContentBuilder
import net.fortuna.ical4j.vcard.Entity
import spock.lang.Specification

class JSCardSerializerTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JSCalendarModule())

    ContentBuilder builder = new ContentBuilder()

    def 'serializes card with full name and email'() {
        given:
        Entity card = builder.entity {
            fn 'Jane Doe'
            uid '1'
            email 'jane@example.com'
        }

        when:
        JsonNode node = mapper.valueToTree(card)

        then:
        node.get('@type').asText() == 'Card'
        node.get('name').get('full').asText() == 'Jane Doe'
        def emails = node.get('emails')
        emails.size() == 1
        emails.fields().next().value.get('address').asText() == 'jane@example.com'
    }
}
