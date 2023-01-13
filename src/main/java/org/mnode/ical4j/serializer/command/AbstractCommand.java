package org.mnode.ical4j.serializer.command;

import picocli.CommandLine;

import java.net.URL;

public abstract class AbstractCommand implements Runnable {

    @CommandLine.Option(names = {"-X", "--query"})
    protected String query;

    @CommandLine.ArgGroup(multiplicity = "1")
    protected Input input;

    public static class Input {
        @CommandLine.Option(names = {"-U", "--url"}, required = true)
        protected URL url;

        @CommandLine.Option(names = {"-F", "--file"}, required = true)
        protected String filename;

        @CommandLine.Option(names = {"--stdin"}, required = true)
        protected boolean stdin;

        public Input(URL url) {
            this.url = url;
        }

        public Input(String filename) {
            this.filename = filename;
        }

        public Input() {
            this.stdin = true;
        }
    }

}
