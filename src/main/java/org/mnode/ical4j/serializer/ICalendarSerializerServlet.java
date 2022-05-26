package org.mnode.ical4j.serializer;

import net.fortuna.ical4j.data.ParserException;
import org.apache.commons.io.IOUtils;
import org.mnode.ical4j.serializer.command.SerializeCalendarCommand;
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
        String urlParam = req.getParameter("url");
        if (urlParam != null) {
            try {
                URL url = new URL(urlParam);
                resp.setContentType("application/json");
                resp.getWriter().println(new SerializeCalendarCommand(url).serialize());
            } catch (ParserException e) {
                throw new ServletException(e);
            }
        } else {
            resp.getOutputStream().write(form);
        }
    }
}
