package org.mnode.ical4j.serializer;

import org.mnode.ical4j.serializer.command.JettyRunCommand;
import org.mnode.ical4j.serializer.command.SerializeCalendarCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "serializer", description = "iCal4j Serializer",
        subcommands = {SerializeCalendarCommand.class, JettyRunCommand.class})
public class ICalendarSerializerMain implements Runnable {

    @Override
    public void run() {
        System.out.println("iCal4j Serializer. Usage: serializer <subcommand> [options]");
    }

    public static void main(String[] args) throws Exception {
        new CommandLine(new ICalendarSerializerMain()).execute(args);
    }
}
