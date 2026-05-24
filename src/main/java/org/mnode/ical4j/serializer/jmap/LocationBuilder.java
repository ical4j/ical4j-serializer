package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.fortuna.ical4j.model.PropertyContainer;
import net.fortuna.ical4j.model.property.Geo;
import net.fortuna.ical4j.model.property.Location;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Builds the JSCalendar {@code locations} map from a component's
 * {@code LOCATION} and {@code GEO} properties.
 */
final class LocationBuilder {

    private LocationBuilder() {
    }

    static void applyTo(PropertyContainer source, ObjectNode target) {
        List<Location> locations = source.getProperties("LOCATION");
        Optional<Geo> geo = source.getProperty("GEO");
        if (locations.isEmpty() && geo.isEmpty()) {
            return;
        }
        ObjectNode container = target.objectNode();
        int index = 1;
        for (Location location : locations) {
            ObjectNode node = target.objectNode();
            node.put("@type", "Location");
            node.put("name", location.getValue());
            if (index == 1 && geo.isPresent()) {
                node.put("coordinates", geoToUri(geo.get()));
            }
            container.set("location-" + index, node);
            index++;
        }
        if (locations.isEmpty() && geo.isPresent()) {
            ObjectNode node = target.objectNode();
            node.put("@type", "Location");
            node.put("coordinates", geoToUri(geo.get()));
            container.set("location-1", node);
        }
        target.set("locations", container);
    }

    private static String geoToUri(Geo geo) {
        BigDecimal lat = geo.getLatitude();
        BigDecimal lon = geo.getLongitude();
        return "geo:" + lat.toPlainString() + "," + lon.toPlainString();
    }
}
