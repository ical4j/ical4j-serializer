package org.mnode.ical4j.json

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.fortuna.ical4j.model.Calendar

class JCalSerializerTest extends AbstractSerializerTest {

    def 'test calendar serialization'() {
        given: 'an object mapper'
        SimpleModule module = []
        module.addSerializer(Calendar, new JCalSerializer())
        ObjectMapper mapper = []
        mapper.registerModule(module)

        when: 'the calendar is serialized'
        String serialized = mapper.writeValueAsString(calendar)

        then: 'serialized string matches expected value'
        serialized == expectedSerialized

        where:
        calendar    | expectedSerialized
        calendar1   | '["vcalendar",[["prodid",{},"text","-//Ben Fortuna//iCal4j 1.0//EN"],["version",{},"text","2.0"],["uid",{},"text","123"]],[["vevent",[["uid",{},"text","1"],["summary",{},"text","Test Event 1"],["organizer",{},"cal-address","johnd@example.com"],["dtstart",{"value":"date"},"date","2009-08-10"],["action",{},"text","DISPLAY"],["attach",{"encoding":"base64","value":"binary"},"binary","QlNEIDMtQ2xhdXNlIExpY2Vuc2UKCkNvcHlyaWdodCAoYykgMjAyMSwgaUNhbDRqCkFsbCByaWdodHMgcmVzZXJ2ZWQuCgpSZWRpc3RyaWJ1dGlvbiBhbmQgdXNlIGluIHNvdXJjZSBhbmQgYmluYXJ5IGZvcm1zLCB3aXRoIG9yIHdpdGhvdXQKbW9kaWZpY2F0aW9uLCBhcmUgcGVybWl0dGVkIHByb3ZpZGVkIHRoYXQgdGhlIGZvbGxvd2luZyBjb25kaXRpb25zIGFyZSBtZXQ6CgoxLiBSZWRpc3RyaWJ1dGlvbnMgb2Ygc291cmNlIGNvZGUgbXVzdCByZXRhaW4gdGhlIGFib3ZlIGNvcHlyaWdodCBub3RpY2UsIHRoaXMKICAgbGlzdCBvZiBjb25kaXRpb25zIGFuZCB0aGUgZm9sbG93aW5nIGRpc2NsYWltZXIuCgoyLiBSZWRpc3RyaWJ1dGlvbnMgaW4gYmluYXJ5IGZvcm0gbXVzdCByZXByb2R1Y2UgdGhlIGFib3ZlIGNvcHlyaWdodCBub3RpY2UsCiAgIHRoaXMgbGlzdCBvZiBjb25kaXRpb25zIGFuZCB0aGUgZm9sbG93aW5nIGRpc2NsYWltZXIgaW4gdGhlIGRvY3VtZW50YXRpb24KICAgYW5kL29yIG90aGVyIG1hdGVyaWFscyBwcm92aWRlZCB3aXRoIHRoZSBkaXN0cmlidXRpb24uCgozLiBOZWl0aGVyIHRoZSBuYW1lIG9mIHRoZSBjb3B5cmlnaHQgaG9sZGVyIG5vciB0aGUgbmFtZXMgb2YgaXRzCiAgIGNvbnRyaWJ1dG9ycyBtYXkgYmUgdXNlZCB0byBlbmRvcnNlIG9yIHByb21vdGUgcHJvZHVjdHMgZGVyaXZlZCBmcm9tCiAgIHRoaXMgc29mdHdhcmUgd2l0aG91dCBzcGVjaWZpYyBwcmlvciB3cml0dGVuIHBlcm1pc3Npb24uCgpUSElTIFNPRlRXQVJFIElTIFBST1ZJREVEIEJZIFRIRSBDT1BZUklHSFQgSE9MREVSUyBBTkQgQ09OVFJJQlVUT1JTICJBUyBJUyIKQU5EIEFOWSBFWFBSRVNTIE9SIElNUExJRUQgV0FSUkFOVElFUywgSU5DTFVESU5HLCBCVVQgTk9UIExJTUlURUQgVE8sIFRIRQpJTVBMSUVEIFdBUlJBTlRJRVMgT0YgTUVSQ0hBTlRBQklMSVRZIEFORCBGSVRORVNTIEZPUiBBIFBBUlRJQ1VMQVIgUFVSUE9TRSBBUkUKRElTQ0xBSU1FRC4gSU4gTk8gRVZFTlQgU0hBTEwgVEhFIENPUFlSSUdIVCBIT0xERVIgT1IgQ09OVFJJQlVUT1JTIEJFIExJQUJMRQpGT1IgQU5ZIERJUkVDVCwgSU5ESVJFQ1QsIElOQ0lERU5UQUwsIFNQRUNJQUwsIEVYRU1QTEFSWSwgT1IgQ09OU0VRVUVOVElBTApEQU1BR0VTIChJTkNMVURJTkcsIEJVVCBOT1QgTElNSVRFRCBUTywgUFJPQ1VSRU1FTlQgT0YgU1VCU1RJVFVURSBHT09EUyBPUgpTRVJWSUNFUzsgTE9TUyBPRiBVU0UsIERBVEEsIE9SIFBST0ZJVFM7IE9SIEJVU0lORVNTIElOVEVSUlVQVElPTikgSE9XRVZFUgpDQVVTRUQgQU5EIE9OIEFOWSBUSEVPUlkgT0YgTElBQklMSVRZLCBXSEVUSEVSIElOIENPTlRSQUNULCBTVFJJQ1QgTElBQklMSVRZLApPUiBUT1JUIChJTkNMVURJTkcgTkVHTElHRU5DRSBPUiBPVEhFUldJU0UpIEFSSVNJTkcgSU4gQU5ZIFdBWSBPVVQgT0YgVEhFIFVTRQpPRiBUSElTIFNPRlRXQVJFLCBFVkVOIElGIEFEVklTRUQgT0YgVEhFIFBPU1NJQklMSVRZIE9GIFNVQ0ggREFNQUdFLgo="]],[]],["vevent",[["uid",{},"text","2"],["summary",{},"text","Test Event 2"],["organizer",{"cn":"john doe"},"cal-address","johnd@example.com"],["dtstart",{"value":"date"},"date","2010-08-10"],["description",{"x-format":"text/plain"},"text","Test Description 2"],["x-test",{},"unknown","test-value"]],[]]]]'
        calendar2   | '["vcalendar",[["version",{},"text","2.0"],["prodid",{},"text","-//ABC Corporation//NONSGML My Product//EN"],["uid",{},"text","1"],["name",{},"text","Just In"],["description",{},"text",""],["source",{},"uri","https://www.abc.net.au/news/feed/51120/rss.xml"],["url",{},"uri","https://www.abc.net.au/news/justin/"],["image",{},"uri","https://www.abc.net.au/news/image/8413416-1x1-144x144.png"],["last-modified",{},"date-time","2021-03-04T05:52:23Z"]],[["vjournal",[["uid",{},"text","https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856"],["summary",{},"text","The Gold Coast needs 6,500 new homes a year, but where can they be built?"],["description",{},"text","\\n              \\n              <p>The famous coastal city is fast running out of greenfield land to house its growing population, but the community is opposed to higher-density developments in the city.</p>\\n              \\n"],["categories",{},"text","Housing Industry,Rental Housing,Housing,Agribusiness"],["dtstamp",{},"date-time","2021-03-04T05:52:23Z"],["url",{},"uri","https://www.abc.net.au/news/2021-03-04/gold-coast-needs-6,500-new-homes-a-year-housing-crisis/13214856"],["contact",{},"text","Dominic Cansdale"],["image",{},"uri","https://www.abc.net.au/news/image/12721466-3x2-940x627.jpg"]],[]]]]'
    }
}
