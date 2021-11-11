package org.mnode.ical4j.serializer.jsonfeed

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar
import org.mnode.ical4j.serializer.AbstractSerializerTest

class JSONFeedCalendarSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JSONFeedCalendarSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '{"version":"https://jsonfeed.org/version/1.1","items":[{"id":"1","summary":"Test Event 1"},{"id":"2","summary":"Test Event 2","content_text":"Test Description 2"}]}'
        calendar2   | '{"version":"https://jsonfeed.org/version/1.1","items":[{"id":"https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856","summary":"The Gold Coast needs 6,500 new homes a year, but where can they be built?","content_html":"\\n              \\n              <p>The famous coastal city is fast running out of greenfield land to house its growing population, but the community is opposed to higher-density developments in the city.</p>\\n              \\n","date_published":"20210304T055223Z","url":"https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856"}],"title":"Just In","feed_url":"https://www.abc.net.au/news/feed/51120/rss.xml","home_page_url":"https://www.abc.net.au/news/justin/"}'
    }
}
