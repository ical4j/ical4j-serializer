package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import net.fortuna.ical4j.vcard.ContentBuilder
import net.fortuna.ical4j.vcard.Entity
import net.fortuna.ical4j.vcard.property.BDay
import spock.lang.Specification
import spock.lang.Unroll

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

    @Unroll
    def 'BDAY #value becomes anniversary date #expected'() {
        given:
        Entity card = builder.entity {
            fn 'Jane Doe'
            uid '1'
        }
        card.add(new BDay(value))

        when:
        JsonNode node = mapper.valueToTree(card)
        def date = node.get('anniversaries').get('anniversary-1').get('date')

        then:
        node.get('anniversaries').get('anniversary-1').get('kind').asText() == 'birth'
        mapper.convertValue(date, Map) == expected

        where:
        value              | expected
        '19800415'         | ['@type': 'PartialDate', year: 1980, month: 4, day: 15]
        '1980-04'          | ['@type': 'PartialDate', year: 1980, month: 4]
        '1980'             | ['@type': 'PartialDate', year: 1980]
        '--0415'           | ['@type': 'PartialDate', month: 4, day: 15]
        '---15'            | ['@type': 'PartialDate', day: 15]
        '19800415T1030'    | ['@type': 'PartialDate', year: 1980, month: 4, day: 15]
        '19800415T103000Z' | ['@type': 'Timestamp', utc: '1980-04-15T10:30:00Z']
        '19800415T1030+1000' | ['@type': 'Timestamp', utc: '1980-04-15T00:30:00Z']
    }

    def 'BDAY free text is omitted'() {
        given:
        Entity card = builder.entity {
            fn 'Jane Doe'
            uid '1'
        }
        card.add(new BDay('circa 1800'))

        when:
        JsonNode node = mapper.valueToTree(card)

        then:
        !node.has('anniversaries')
    }
}
