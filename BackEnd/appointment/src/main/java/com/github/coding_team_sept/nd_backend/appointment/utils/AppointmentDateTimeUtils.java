package com.github.coding_team_sept.nd_backend.appointment.utils;

import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentDateTimeException;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDateTimeUtils {
    static final String pattern = "yyyy-MM-dd_HH:mm";
    static final int intervalInMinutes = 15; // minutes
    private static final LocalTime closeTime = LocalTime.parse("20:00", DateTimeFormat.forPattern("HH:mm"));
    private static final LocalTime openTime = LocalTime.parse("08:00", DateTimeFormat.forPattern("HH:mm"));


    public DateTime parseString(String datetime) {
        return DateTime.parse(datetime, DateTimeFormat.forPattern(pattern));
    }

    public DateTime getMin(DateTime datetime) {
        return datetime.minusMinutes(intervalInMinutes);
    }

    public DateTime getMax(DateTime datetime) {
        return datetime.plusMinutes(intervalInMinutes);
    }

    public boolean validateAppointmentDateTime(DateTime datetime) throws AppointmentDateTimeException {
        if (datetime.isBeforeNow()) {
            throw new AppointmentDateTimeException("Datetime in the past");
        }

        if (datetime.isAfter(DateTime.now().plusYears(1))) {
            throw new AppointmentDateTimeException("Passed the limit of appointment (1 year)");
        }

        if (!(datetime.isAfter(getDateTimeWithTime(datetime, openTime))
                && datetime.isBefore(getDateTimeWithTime(datetime, closeTime.minusMinutes(intervalInMinutes))))) {
            throw new AppointmentDateTimeException("Out of operation time");
        }
        return true;
    }

    private DateTime getDateTimeWithTime(DateTime datetime, LocalTime time) {
        return (new DateTime(datetime)).withTime(
                time.getHourOfDay(),
                time.getMinuteOfHour(),
                time.getSecondOfMinute(),
                time.getMillisOfSecond()
        );
    }
}
