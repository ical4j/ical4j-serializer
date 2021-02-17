package org.mnode.ical4j.json;

import java.net.URL;
import java.time.ZoneId;
import java.util.Map;

public class LocationBuilder {

    private String name;

    private String description;

    private LocationType locationType;

    private String relativeTo; // start/end

    private ZoneId timeZone;

    private String coordinates;

    private Map<String, URL> links;
}
