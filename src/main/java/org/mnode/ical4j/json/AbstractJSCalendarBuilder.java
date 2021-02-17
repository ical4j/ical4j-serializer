package org.mnode.ical4j.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

public class AbstractJSCalendarBuilder {

    protected String uid;

    protected List<String> relatedTo;

    protected String prodId;

    protected Instant created;
    
    protected Instant updated;

    protected Integer sequence;

    protected String method;

    protected String title;
    
    protected String description;

    protected String descriptionContentType;

    protected Boolean showWithoutTime;

    protected List<LocationBuilder> locations;

    protected List<VirtualLocationBuilder> virtualLocations;

    protected List<LinkBuilder> links;

    protected Locale locale;

    protected List<String> keywords;

    protected List<String> categories;

    protected Color color;

    public static void setProperty(ObjectNode node, String fieldName, String value, boolean mandatory) {
        if (mandatory && value == null) {
            throw new IllegalArgumentException(String.format("Value [%s] cannot be null", fieldName));
        } else if (value != null) {
            node.put(fieldName, value);
        }
    }
}
