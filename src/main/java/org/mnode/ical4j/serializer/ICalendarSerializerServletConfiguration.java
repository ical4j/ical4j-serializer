package org.mnode.ical4j.serializer;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "iCalendar Serializer Servlet", description = "Servlet Configuration for an iCalendar serializer")
@interface ICalendarSerializerServletConfiguration {

    @AttributeDefinition(name = "alias", description = "Servlet alias")
    String alias() default "/serializer";
}
