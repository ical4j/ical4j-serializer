package org.mnode.ical4j.json;

import net.fortuna.ical4j.model.ZoneOffsetAdapter;
import org.apache.commons.codec.StringDecoder;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class JCalDecoder implements StringDecoder {

    public static final JCalDecoder DATE = new JCalDecoder(s ->
            DateTimeFormatter.BASIC_ISO_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE.parse(s)));

    public static final JCalDecoder DATE_TIME = new JCalDecoder(s ->
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss").format(DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(s)));

    public static final JCalDecoder TIME = new JCalDecoder(s ->
            DateTimeFormatter.ofPattern("HHmmss").format(DateTimeFormatter.ISO_LOCAL_TIME.parse(s)));

    public static final JCalDecoder UTCOFFSET = new JCalDecoder(s ->
            new ZoneOffsetAdapter(ZoneOffset.of(s)).toString());

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
