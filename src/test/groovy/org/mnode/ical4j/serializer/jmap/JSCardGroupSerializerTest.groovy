package org.mnode.ical4j.serializer.jmap

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.vcard.ContentBuilder
import net.fortuna.ical4j.vcard.Entity
import spock.lang.Specification

class JSCardGroupSerializerTest extends Specification {

    ObjectMapper mapper = new ObjectMapper().registerModule(new SimpleModule().addSerializer(Entity, new JSCardGroupSerializer()))

    ContentBuilder builder = new ContentBuilder()

    def 'serializes vCard group entity as CardGroup with members'() {
        given:
        Entity group = builder.entity {
            fn 'Marketing'
            uid 'group-1'
            member 'urn:uuid:11111111-1111-1111-1111-111111111111'
            member 'urn:uuid:22222222-2222-2222-2222-222222222222'
        }

        when:
        JsonNode node = mapper.valueToTree(group)

        then:
        node.get('@type').asText() == 'CardGroup'
        node.get('uid').asText() == 'group-1'
        node.get('name').get('full').asText() == 'Marketing'

        def members = node.get('members')
        members.get('urn:uuid:11111111-1111-1111-1111-111111111111').asBoolean()
        members.get('urn:uuid:22222222-2222-2222-2222-222222222222').asBoolean()
    }
}
