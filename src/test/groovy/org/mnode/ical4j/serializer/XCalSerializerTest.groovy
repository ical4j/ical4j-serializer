package org.mnode.ical4j.serializer


import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import net.fortuna.ical4j.model.Calendar

class XCalSerializerTest extends AbstractSerializerTest {

    def 'test calendar XML serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new XCalSerializer())
        XmlMapper mapper = XmlMapper.builder().defaultUseWrapper(true).build()
        mapper.setConfig(mapper.getSerializationConfig().withRootName(
                PropertyName.construct("icalendar", "urn:ietf:params:xml:ns:icalendar-2.0"))
                .with(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME))
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '<icalendar xmlns="urn:ietf:params:xml:ns:icalendar-2.0"><vcalendar><properties><prodid><parameters/><text>-//Ben Fortuna//iCal4j 3.1//EN</text></prodid><version><parameters/><text>2.0</text></version><uid><parameters/><text>123</text></uid></properties><components><vevent><properties><uid><parameters/><text>1</text></uid><summary><parameters/><text>Test Event 1</text></summary><organizer><parameters/><cal-address>johnd@example.com</cal-address></organizer><dtstart><parameters><value>date</value></parameters><date>2009-08-10</date></dtstart><action><parameters/><text>DISPLAY</text></action><attach><parameters><encoding>base64</encoding><value>binary</value></parameters><binary>QlNEIDMtQ2xhdXNlIExpY2Vuc2UKCkNvcHlyaWdodCAoYykgMjAyMSwgaUNhbDRqCkFsbCByaWdodHMgcmVzZXJ2ZWQuCgpSZWRpc3RyaWJ1dGlvbiBhbmQgdXNlIGluIHNvdXJjZSBhbmQgYmluYXJ5IGZvcm1zLCB3aXRoIG9yIHdpdGhvdXQKbW9kaWZpY2F0aW9uLCBhcmUgcGVybWl0dGVkIHByb3ZpZGVkIHRoYXQgdGhlIGZvbGxvd2luZyBjb25kaXRpb25zIGFyZSBtZXQ6CgoxLiBSZWRpc3RyaWJ1dGlvbnMgb2Ygc291cmNlIGNvZGUgbXVzdCByZXRhaW4gdGhlIGFib3ZlIGNvcHlyaWdodCBub3RpY2UsIHRoaXMKICAgbGlzdCBvZiBjb25kaXRpb25zIGFuZCB0aGUgZm9sbG93aW5nIGRpc2NsYWltZXIuCgoyLiBSZWRpc3RyaWJ1dGlvbnMgaW4gYmluYXJ5IGZvcm0gbXVzdCByZXByb2R1Y2UgdGhlIGFib3ZlIGNvcHlyaWdodCBub3RpY2UsCiAgIHRoaXMgbGlzdCBvZiBjb25kaXRpb25zIGFuZCB0aGUgZm9sbG93aW5nIGRpc2NsYWltZXIgaW4gdGhlIGRvY3VtZW50YXRpb24KICAgYW5kL29yIG90aGVyIG1hdGVyaWFscyBwcm92aWRlZCB3aXRoIHRoZSBkaXN0cmlidXRpb24uCgozLiBOZWl0aGVyIHRoZSBuYW1lIG9mIHRoZSBjb3B5cmlnaHQgaG9sZGVyIG5vciB0aGUgbmFtZXMgb2YgaXRzCiAgIGNvbnRyaWJ1dG9ycyBtYXkgYmUgdXNlZCB0byBlbmRvcnNlIG9yIHByb21vdGUgcHJvZHVjdHMgZGVyaXZlZCBmcm9tCiAgIHRoaXMgc29mdHdhcmUgd2l0aG91dCBzcGVjaWZpYyBwcmlvciB3cml0dGVuIHBlcm1pc3Npb24uCgpUSElTIFNPRlRXQVJFIElTIFBST1ZJREVEIEJZIFRIRSBDT1BZUklHSFQgSE9MREVSUyBBTkQgQ09OVFJJQlVUT1JTICJBUyBJUyIKQU5EIEFOWSBFWFBSRVNTIE9SIElNUExJRUQgV0FSUkFOVElFUywgSU5DTFVESU5HLCBCVVQgTk9UIExJTUlURUQgVE8sIFRIRQpJTVBMSUVEIFdBUlJBTlRJRVMgT0YgTUVSQ0hBTlRBQklMSVRZIEFORCBGSVRORVNTIEZPUiBBIFBBUlRJQ1VMQVIgUFVSUE9TRSBBUkUKRElTQ0xBSU1FRC4gSU4gTk8gRVZFTlQgU0hBTEwgVEhFIENPUFlSSUdIVCBIT0xERVIgT1IgQ09OVFJJQlVUT1JTIEJFIExJQUJMRQpGT1IgQU5ZIERJUkVDVCwgSU5ESVJFQ1QsIElOQ0lERU5UQUwsIFNQRUNJQUwsIEVYRU1QTEFSWSwgT1IgQ09OU0VRVUVOVElBTApEQU1BR0VTIChJTkNMVURJTkcsIEJVVCBOT1QgTElNSVRFRCBUTywgUFJPQ1VSRU1FTlQgT0YgU1VCU1RJVFVURSBHT09EUyBPUgpTRVJWSUNFUzsgTE9TUyBPRiBVU0UsIERBVEEsIE9SIFBST0ZJVFM7IE9SIEJVU0lORVNTIElOVEVSUlVQVElPTikgSE9XRVZFUgpDQVVTRUQgQU5EIE9OIEFOWSBUSEVPUlkgT0YgTElBQklMSVRZLCBXSEVUSEVSIElOIENPTlRSQUNULCBTVFJJQ1QgTElBQklMSVRZLApPUiBUT1JUIChJTkNMVURJTkcgTkVHTElHRU5DRSBPUiBPVEhFUldJU0UpIEFSSVNJTkcgSU4gQU5ZIFdBWSBPVVQgT0YgVEhFIFVTRQpPRiBUSElTIFNPRlRXQVJFLCBFVkVOIElGIEFEVklTRUQgT0YgVEhFIFBPU1NJQklMSVRZIE9GIFNVQ0ggREFNQUdFLgo=</binary></attach></properties><components/></vevent></components></vcalendar></icalendar>'
        calendar2   | '''<icalendar xmlns="urn:ietf:params:xml:ns:icalendar-2.0"><vcalendar><properties><version><parameters/><text>2.0</text></version><prodid><parameters/><text>-//ABC Corporation//NONSGML My Product//EN</text></prodid><uid><parameters/><text>1</text></uid><name><parameters/><text>Just In</text></name><description><parameters/><text></text></description><source><parameters/><uri>https://www.abc.net.au/news/feed/51120/rss.xml</uri></source><url><parameters/><uri>https://www.abc.net.au/news/justin/</uri></url><image><parameters/><uri>https://www.abc.net.au/news/image/8413416-1x1-144x144.png</uri></image><last-modified><parameters/><date-time>2021-03-04T05:52:23Z</date-time></last-modified></properties><components><vjournal><properties><uid><parameters/><text>https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856</text></uid><summary><parameters/><text>The Gold Coast needs 6,500 new homes a year, but where can they be built?</text></summary><styled-description><parameters><value>text</value></parameters><text>
              
              &lt;p>The famous coastal city is fast running out of greenfield land to house its growing population, but the community is opposed to higher-density developments in the city.&lt;/p>
              
</text></styled-description><categories><parameters/><text>Housing Industry,Rental Housing,Housing,Agribusiness</text></categories><dtstamp><parameters/><date-time>2021-03-04T05:52:23Z</date-time></dtstamp><url><parameters/><uri>https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856</uri></url><contact><parameters/><text>Dominic Cansdale</text></contact><image><parameters/><uri>https://www.abc.net.au/news/image/12721466-3x2-940x627.jpg</uri></image></properties><components/></vjournal></components></vcalendar></icalendar>'''
    }
}
