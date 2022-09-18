package com.github.coding_team_sept.nd_backend.appointment.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtils {
    static final String pattern = "yyyy-MM-dd_HH:mm";
    static final Long interval = 15L;

    public DateTime parseString(String datetime) {
        return DateTime.parse(datetime, DateTimeFormat.forPattern(pattern));
    }

    public DateTime getMin(DateTime dateTime) {
        return dateTime.minus(interval);
    }

    public DateTime getMax(DateTime dateTime) {
        return dateTime.plus(interval);
    }
}
