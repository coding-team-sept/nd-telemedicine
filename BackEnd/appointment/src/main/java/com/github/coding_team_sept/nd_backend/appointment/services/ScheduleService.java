package com.github.coding_team_sept.nd_backend.appointment.services;

import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentConflictException;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentDateTimeException;
import com.github.coding_team_sept.nd_backend.appointment.repositories.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.utils.AppointmentDateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

@Service
public record ScheduleService(
        AppointmentRepository appointmentRepo,
        AppointmentDateTimeUtils datetimeUtils
) {
    public static final LocalTime closeTime = LocalTime.parse("20:00", DateTimeFormat.forPattern("HH:mm"));
    public static final LocalTime openTime = LocalTime.parse("08:00", DateTimeFormat.forPattern("HH:mm"));

    public void validateAppointmentDateTime(
            DateTime datetime
    ) throws AppointmentDateTimeException {
        if (datetime.isBeforeNow()) {
            throw new AppointmentDateTimeException("Datetime in the past");
        }

        if (datetime.isAfter(DateTime.now().plusYears(1))) {
            throw new AppointmentDateTimeException("Passed the limit of appointment (1 year)");
        }

        if (!(datetime.isAfter(datetime.withTime(openTime))
                && datetime.isBefore(datetime.withTime(closeTime.minusMinutes(AppointmentDateTimeUtils.intervalInMinutes))))) {
            throw new AppointmentDateTimeException("Out of operation time");
        }
    }

    public void checkPatientAvailability(
            DateTime appointmentDatetime,
            Long patientId
    ) throws AppointmentConflictException {
        if (appointmentRepo.existsAppointmentByPatientIdAndAppointmentTimeBetween(
                patientId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )) {
            throw new AppointmentConflictException("Patient is occupied");
        }
    }

    public void checkDoctorAvailability(
            DateTime appointmentDatetime,
            Long doctorId
    ) throws AppointmentConflictException {
        if (appointmentRepo.existsAppointmentByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )) {
            throw new AppointmentConflictException("Doctor is occupied");
        }
    }
}
