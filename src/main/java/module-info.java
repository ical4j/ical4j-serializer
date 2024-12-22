module ical4j.serializer {
    requires java.base;
    requires ical4j.core;
    requires ical4j.vcard;
    requires ical4j.extensions;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires org.apache.commons.codec;

    exports org.mnode.ical4j.serializer;
    exports org.mnode.ical4j.serializer.jotn;
    exports org.mnode.ical4j.serializer.jotn.component;
    exports org.mnode.ical4j.serializer.jotn.property;
    exports org.mnode.ical4j.serializer.atom;
    exports org.mnode.ical4j.serializer.jmap;
    exports org.mnode.ical4j.serializer.jsonld;
}