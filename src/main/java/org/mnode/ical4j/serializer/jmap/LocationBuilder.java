package org.mnode.ical4j.serializer.jmap;

import net.fortuna.ical4j.model.LocationType;

import java.net.URL;
import java.time.ZoneId;
import java.util.Map;

/**
 * Represents a location in a JMAP context, typically used for calendar events or tasks.
 * This class encapsulates various attributes of a location, including its name, description,
 * type, relative position, time zone, coordinates, and associated links.
 */
public class LocationBuilder {

    private String name;

    private String description;

    private LocationType locationType;

    private String relativeTo; // start/end

    private ZoneId timeZone;

    private String coordinates;

    private Map<String, URL> links;
}
