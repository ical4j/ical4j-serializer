package org.mnode.ical4j.serializer.openid;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.fortuna.ical4j.vcard.Property;
import net.fortuna.ical4j.vcard.VCard;
import net.fortuna.ical4j.vcard.property.Address;
import net.fortuna.ical4j.vcard.property.Email;
import net.fortuna.ical4j.vcard.property.Name;
import net.fortuna.ical4j.vcard.property.Photo;
import org.mnode.ical4j.serializer.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;

/**
 * See https://openid.net/specs/openid-connect-core-1_0.html#StandardClaims for mapping.
 */
public class JwtClaimsMapper extends StdDeserializer<VCard> implements JsonMapper {

    public JwtClaimsMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public VCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        VCard card = new VCard();
        // should have at least one field..
        assertNextToken(p, JsonToken.FIELD_NAME);
        while (p.getCurrentToken() != JsonToken.END_OBJECT) {
            try {
                card.getProperties().add(parseProperty(p));
            } catch (URISyntaxException | ParseException e) {
                throw new IllegalArgumentException(e);
            }
            p.nextToken();
        }
        return card;
    }

    private Property parseProperty(JsonParser p) throws IOException, URISyntaxException, ParseException {
        String propertyName = p.currentName();
        assertNextToken(p, JsonToken.VALUE_STRING);
        switch (propertyName) {
            case "name": return new Name(p.getValueAsString());
            case "picture": return new Photo(URI.create(p.getValueAsString()));
            case "email": return new Email(p.getValueAsString());
            case "address": return parseAddress((JsonNode) p.getCurrentValue());
            default: return null;
        }
    }

    private Address parseAddress(JsonNode n) {
        String formatted = n.get("formatted").asText();
        String street = n.get("street_address").asText();
        String locality = n.get("locality").asText();
        String region = n.get("region").asText();
        String postal_code = n.get("postal_code").asText();
        String country = n.get("country").asText();

        return new Address(null, null, street, locality, region, postal_code, country);
    }
}
