package org.mnode.ical4j.serializer.jmap;

import com.fasterxml.jackson.databind.module.SimpleModule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VJournal;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.vcard.Entity;

/**
 * Jackson {@link SimpleModule} that registers the JSCalendar / JSContact
 * serializers in {@code org.mnode.ical4j.serializer.jmap}.
 *
 * <p>Register once on an {@link com.fasterxml.jackson.databind.ObjectMapper}:</p>
 * <pre>
 * mapper.registerModule(new JSCalendarModule());
 * </pre>
 *
 * <p>Supported subset (RFC 8984 / RFC 9610):</p>
 * <ul>
 *   <li>{@link VEvent} → {@code jsevent}</li>
 *   <li>{@link VToDo} → {@code jstask}</li>
 *   <li>{@link VJournal} → {@code jsevent} with {@code freeBusyStatus:"free"}</li>
 *   <li>{@link Calendar} → {@code Group} with {@code entries} of child components</li>
 *   <li>{@link Entity} → {@code Card} (registered serializer; for
 *   {@code KIND:group} entities use {@link JSCardGroupSerializer} explicitly)</li>
 * </ul>
 *
 * <p>Known gaps tracked as follow-ups:</p>
 * <ul>
 *   <li>Recurrence overrides for {@code RECURRENCE-ID} (modified instances)</li>
 *   <li>{@code useDefaultAlerts}, {@code localizations}</li>
 *   <li>JSContact extended properties (anniversaries beyond {@code BDAY},
 *   pronouns, social profiles)</li>
 * </ul>
 */
public class JSCalendarModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public JSCalendarModule() {
        super("jscalendar");
        addSerializer(VEvent.class, new JSEventSerializer());
        addSerializer(VToDo.class, new JSTaskSerializer());
        addSerializer(VJournal.class, new JSJournalSerializer());
        addSerializer(Calendar.class, new JSGroupSerializer());
        addSerializer(Entity.class, new JSCardSerializer());
    }
}
