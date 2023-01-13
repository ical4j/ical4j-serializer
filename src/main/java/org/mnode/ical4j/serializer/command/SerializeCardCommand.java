package org.mnode.ical4j.serializer.command;

import picocli.CommandLine;

@CommandLine.Command(name = "card", description = "Transform input vCard data to specified format",
        subcommands = {CommandLine.HelpCommand.class})
public class SerializeCardCommand extends AbstractCommand {

    @Override
    public void run() {

    }
}
