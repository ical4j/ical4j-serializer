package org.mnode.ical4j.serializer.jmap;

import java.net.URL;

/**
 * Represents a link in a JMAP context, typically used for linking resources such as enclosures, descriptions, or icons.
 * This class encapsulates the relationship type (rel) and the URL of the resource.
 */
public class LinkBuilder {

    private String rel; // enclosure/describeBy/icon

    private URL url;
}
