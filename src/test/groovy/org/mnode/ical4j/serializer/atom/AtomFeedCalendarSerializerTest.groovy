package org.mnode.ical4j.serializer.atom

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import net.fortuna.ical4j.model.Calendar
import org.mnode.ical4j.serializer.AbstractSerializerTest

class AtomFeedCalendarSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new AtomFeedCalendarSerializer())
        XmlMapper mapper = XmlMapper.builder().defaultUseWrapper(true).build()
        mapper.setConfig(mapper.getSerializationConfig().withRootName(
                PropertyName.construct("xmlns", "http://www.w3.org/2005/Atom"))
                .with(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME))
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '<xmlns xmlns="http://www.w3.org/2005/Atom"><items><id>1</id><summary>Test Event 1</summary></items><items><id>2</id><summary>Test Event 2</summary><content_text>Test Description 2</content_text></items></xmlns>'
        calendar2   | '''<xmlns xmlns="http://www.w3.org/2005/Atom"><items><id>https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856</id><summary>The Gold Coast needs 6,500 new homes a year, but where can they be built?</summary><content_html>
              
              &lt;p>The famous coastal city is fast running out of greenfield land to house its growing population, but the community is opposed to higher-density developments in the city.&lt;/p>
              
</content_html><date_published>20210304T055223Z</date_published><url>https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856</url></items><title>Just In</title><feed_url>https://www.abc.net.au/news/feed/51120/rss.xml</feed_url><home_page_url>https://www.abc.net.au/news/justin/</home_page_url></xmlns>'''
    }
}
