package org.mnode.ical4j.serializer;

import org.mnode.ical4j.serializer.command.JettyRunCommand;
import org.mnode.ical4j.serializer.command.SerializeCalendarCommand;
import org.mnode.ical4j.serializer.command.VersionProvider;
import picocli.CommandLine;

@CommandLine.Command(name = "serializer", description = "iCal4j Serializer",
        subcommands = {SerializeCalendarCommand.class, JettyRunCommand.class},
        mixinStandardHelpOptions = true, versionProvider = VersionProvider.class)
public class ICalendarSerializerMain {

    public static void main(String[] args) throws Exception {
        new CommandLine(new ICalendarSerializerMain()).execute(args);
    }
}
