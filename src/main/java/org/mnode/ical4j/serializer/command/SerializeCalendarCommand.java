package org.mnode.ical4j.serializer.command;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.util.Calendars;
import org.mnode.ical4j.serializer.JCalSerializer;
import org.mnode.ical4j.serializer.XCalSerializer;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "calendar", description = "Transform input calendar data to specified format",
        subcommands = {CommandLine.HelpCommand.class})
public class SerializeCalendarCommand extends AbstractCommand {

    public enum SerializationFormat {
        JCAL, XCAL;
    }
    @CommandLine.Option(names = {"-X", "--format"}, defaultValue = "JCAL")
    private SerializationFormat serializationFormat;

    @CommandLine.Option(names = {"-P", "--pretty-print"})
    private boolean prettyPrint;

    public SerializeCalendarCommand() {
    }

    public SerializeCalendarCommand withInput(Input input) {
        this.input = input;
        return this;
    }

    public String serialize() throws IOException, ParserException {
        Calendar cal = null;
        if (input.filename != null) {
            cal = Calendars.load(input.filename);
        } else if (input.url != null) {
            cal = Calendars.load(input.url);
        } else if (input.stdin) {
            final CalendarBuilder builder = new CalendarBuilder();
            cal = builder.build(System.in);
        }
//        CalendarBuilder builder = new CalendarBuilder();
//        UnfoldingReader reader = new UnfoldingReader(new InputStreamReader(url.openStream()), true);
//        Calendar cal = builder.build(reader);
        ObjectMapper mapper;

        SimpleModule module = new SimpleModule();
        switch (serializationFormat) {
            case XCAL:
                module.addSerializer(Calendar.class, new XCalSerializer(null));
                mapper = XmlMapper.builder().defaultUseWrapper(true).build();
                mapper.setConfig(mapper.getSerializationConfig().withRootName(
                                PropertyName.construct("icalendar", "urn:ietf:params:xml:ns:icalendar-2.0"))
                        .with(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME));

                break;

            case JCAL:
            default:
                module.addSerializer(Calendar.class, new JCalSerializer(null));
                mapper = new ObjectMapper();
                break;
        }
        mapper.registerModule(module);

        if (prettyPrint) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cal);
        } else {
            return mapper.writeValueAsString(cal);
        }
    }

    @Override
    public void run() {
        try {
            System.out.print(serialize());
        } catch (IOException | ParserException e) {
            throw new RuntimeException(e);
        }
    }
}
