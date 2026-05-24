package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import net.fortuna.ical4j.model.ContentBuilder
import net.fortuna.ical4j.model.component.VEvent
import spock.lang.Specification
import spock.lang.Unroll

class JSCalendarPropertyMapperTest extends Specification {

    ContentBuilder builder = new ContentBuilder()

    ObjectNode node() {
        new ObjectMapper().createObjectNode()
    }

    def 'applyUid copies the UID value'() {
        given:
        VEvent event = builder.vevent { uid 'abc' }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyUid(event, target)

        then:
        target.get('uid').asText() == 'abc'
    }

    def 'applyTitle copies SUMMARY'() {
        given:
        VEvent event = builder.vevent { summary 'Hello' }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyTitle(event, target)

        then:
        target.get('title').asText() == 'Hello'
    }

    def 'applyTimestamps writes created/updated as UTC strings'() {
        given:
        VEvent event = builder.vevent {
            created '20240101T120000Z'
            lastmodified '20240210T080000Z'
        }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyTimestamps(event, target)

        then:
        target.get('created').asText() == '2024-01-01T12:00:00Z'
        target.get('updated').asText() == '2024-02-10T08:00:00Z'
    }

    def 'applyCategoriesAsKeywords merges multiple CATEGORIES'() {
        given:
        VEvent event = builder.vevent {
            categories 'meeting,work'
            categories 'urgent'
        }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyCategoriesAsKeywords(event, target)

        then:
        target.get('keywords').get('meeting').asBoolean()
        target.get('keywords').get('work').asBoolean()
        target.get('keywords').get('urgent').asBoolean()
    }

    @Unroll
    def 'applyStatus remaps #raw to #expected'() {
        given:
        VEvent event = builder.vevent { status raw }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyStatus(event, target)

        then:
        target.get('status').asText() == expected

        where:
        raw            | expected
        'CONFIRMED'    | 'confirmed'
        'TENTATIVE'    | 'tentative'
        'CANCELLED'    | 'cancelled'
        'IN-PROCESS'   | 'in-process'
        'COMPLETED'    | 'completed'
        'NEEDS-ACTION' | 'needs-action'
    }

    def 'applyPrivacy lowercases CLASS value'() {
        given:
        VEvent event = builder.vevent { 'class' 'PRIVATE' }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyPrivacy(event, target)

        then:
        target.get('privacy').asText() == 'private'
    }

    def 'applyLinkFromUrl emits a links.url entry with rel=about'() {
        given:
        VEvent event = builder.vevent { url 'https://example.com/event' }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyLinkFromUrl(event, target)

        then:
        def link = target.get('links').get('url')
        link.get('@type').asText() == 'Link'
        link.get('href').asText() == 'https://example.com/event'
        link.get('rel').asText() == 'about'
    }

    def 'helpers are no-ops when source property is absent'() {
        given:
        VEvent event = builder.vevent { uid '1' }
        ObjectNode target = node()

        when:
        JSCalendarPropertyMapper.applyTitle(event, target)
        JSCalendarPropertyMapper.applyDescription(event, target)
        JSCalendarPropertyMapper.applySequence(event, target)
        JSCalendarPropertyMapper.applyPriority(event, target)
        JSCalendarPropertyMapper.applyStatus(event, target)
        JSCalendarPropertyMapper.applyCategoriesAsKeywords(event, target)
        JSCalendarPropertyMapper.applyColor(event, target)
        JSCalendarPropertyMapper.applyPrivacy(event, target)
        JSCalendarPropertyMapper.applyLinkFromUrl(event, target)

        then:
        target.size() == 0
    }
}
