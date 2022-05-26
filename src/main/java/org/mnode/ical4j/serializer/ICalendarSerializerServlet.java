package org.mnode.ical4j.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.data.UnfoldingReader;
import net.fortuna.ical4j.model.Calendar;
import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

@Component(
        service = {HttpServlet.class, Servlet.class},
        property = {"service.description=iCalendar Serializer Servlet"}
)
@Designate(ocd = ICalendarSerializerServletConfiguration.class, factory = true)
public class ICalendarSerializerServlet extends HttpServlet {

    private byte[] form;

    @Override
    public void init() throws ServletException {
        super.init();
        try (InputStream data = getClass().getResourceAsStream("/form.html")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.form = IOUtils.toByteArray(Objects.requireNonNull(data));
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getParameter("url");
        if (url != null) {
            try {
                CalendarBuilder builder = new CalendarBuilder();
                UnfoldingReader reader = new UnfoldingReader(new InputStreamReader(new URL(url).openStream()), true);
//                Calendar cal = Calendars.load(new URL(url));
                Calendar cal = builder.build(reader);

                SimpleModule module = new SimpleModule();
                module.addSerializer(Calendar.class, new JCalSerializer(null));
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(module);

                resp.setContentType("application/json");
                resp.getWriter().println(mapper.writeValueAsString(cal));
            } catch (ParserException e) {
                throw new ServletException(e);
            }
        } else {
            resp.getOutputStream().write(form);
        }
    }
}
