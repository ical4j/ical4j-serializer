package org.mnode.ical4j.serializer;

import org.apache.commons.codec.StringEncoder;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * Support for encoding iCalendar value strings as JCal value strings.
 */
public class JCalEncoder implements StringEncoder {

    public static final JCalEncoder DATE = new JCalEncoder(s ->
            DateTimeFormatter.ISO_LOCAL_DATE.format(
                    DateTimeFormatter.BASIC_ISO_DATE.parse(s)));

    public static final JCalEncoder DATE_TIME = new JCalEncoder(s ->
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[X]").format(
                    DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss[X]").parse(s)));

    public static final JCalEncoder TIME = new JCalEncoder(s ->
            DateTimeFormatter.ofPattern("HH:mm:ss[X]").format(
                    DateTimeFormatter.ofPattern("HHmmss[X]").parse(s)));

    public static final JCalEncoder UTCOFFSET = new JCalEncoder(s ->
            ZoneOffset.of(s).toString());

    private final Function<String, String> function;

    public JCalEncoder(Function<String, String> function) {
        this.function = function;
    }

    @Override
    public String encode(String source) {
        return function.apply(source);
    }

    @Override
    public Object encode(Object source) {
        return encode(source.toString());
    }
}
