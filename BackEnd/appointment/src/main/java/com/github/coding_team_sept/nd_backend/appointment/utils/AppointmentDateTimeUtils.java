package com.github.coding_team_sept.nd_backend.appointment.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDateTimeUtils {
    static public final String pattern = "yyyy-MM-dd_HH:mm";
    static public final int intervalInMinutes = 15; // minutes

    public DateTime parseString(String datetime) {
        return DateTime.parse(datetime, DateTimeFormat.forPattern(pattern));
    }

    public DateTime getMin(DateTime datetime) {
        return datetime.minusMinutes(intervalInMinutes);
    }

    public DateTime getMax(DateTime datetime) {
        return datetime.plusMinutes(intervalInMinutes);
    }
}
