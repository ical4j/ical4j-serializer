package org.mnode.ical4j.serializer;

import net.fortuna.ical4j.model.ZoneOffsetAdapter;
import org.apache.commons.codec.StringDecoder;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.function.Function;

/**
 * Support for decoding JCal value strings to iCalendar value strings.
 */
public class JCalDecoder implements StringDecoder {

    public static final JCalDecoder DATE = new JCalDecoder(s ->
            DateTimeFormatter.BASIC_ISO_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE.parse(s)));

    public static final JCalDecoder DATE_TIME = new JCalDecoder(s ->
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX").withZone(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("yyyy'-'MM'-'dd'T'HH':'mm':'ss[X]")
                            .parseBest(s, new InstantTemporalQuery(), new LocalDateTimeTemporalQuery())));

    public static final JCalDecoder INSTANT = new JCalDecoder(s ->
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC)
                    .format(DateTimeFormatter.ISO_INSTANT.parse(s)));

    public static final JCalDecoder TIME = new JCalDecoder(s ->
            DateTimeFormatter.ofPattern("HHmmss").format(DateTimeFormatter.ISO_LOCAL_TIME.parse(s)));

    public static final JCalDecoder UTCOFFSET = new JCalDecoder(s ->
            new ZoneOffsetAdapter(ZoneOffset.of(s)).toString());

    private static class LocalDateTimeTemporalQuery implements TemporalQuery<LocalDateTime>, Serializable {
        @Override
        public LocalDateTime queryFrom(TemporalAccessor temporal) {
            return LocalDateTime.from(temporal);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj != null && getClass() == obj.getClass();
        }
    }

    private static class InstantTemporalQuery implements TemporalQuery<Instant>, Serializable {
        @Override
        public Instant queryFrom(TemporalAccessor temporal) {
            return Instant.from(temporal);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            return obj != null && getClass() == obj.getClass();
        }
    }

    private final Function<String, String> function;

    public JCalDecoder(Function<String, String> function) {
        this.function = function;
    }

    @Override
    public String decode(String source) {
        return function.apply(source);
    }

    @Override
    public Object decode(Object source) {
        return decode(source.toString());
    }
}
