package org.mnode.ical4j.serializer.command;

import net.fortuna.ical4j.model.Calendar;
import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {

    private String serializerVersion;

    private String ical4jVersion;

    private String javaVersion;

    public VersionProvider() {
        serializerVersion = getClass().getPackage().getImplementationVersion();
        ical4jVersion = Calendar.class.getPackage().getImplementationVersion();
        javaVersion = System.getProperty("java.version");
    }

    @Override
    public String[] getVersion() throws Exception {
        return new String[] {"iCal4j Serializer " + serializerVersion,
                "\niCal4j: " + ical4jVersion, "\nJVM: " + javaVersion
        };
    }
}
