package org.mnode.ical4j.serializer.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import org.mnode.ical4j.serializer.JCalSerializer;
import picocli.CommandLine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@CommandLine.Command(name = "calendar", description = "Transform input calendar data to specified format")
public class SerializeCalendarCommand implements Runnable {

    @CommandLine.Option(names = {"-U", "--url"})
    private URL url;

    @CommandLine.Option(names = {"-P", "--pretty-print"})
    private boolean prettyPrint;

    public SerializeCalendarCommand() {
    }

    public SerializeCalendarCommand(URL url) {
        this.url = url;
    }

    public String serialize() throws IOException, ParserException {
        CalendarBuilder builder = new CalendarBuilder();
        UnfoldingReader reader = new UnfoldingReader(new InputStreamReader(url.openStream()), true);
//                Calendar cal = Calendars.load(new URL(url));
        Calendar cal = builder.build(reader);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Calendar.class, new JCalSerializer(null));
        ObjectMapper mapper = new ObjectMapper();
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
