package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import org.springframework.http.HttpStatus;

public class AppointmentConflictException extends AppException {
    public AppointmentConflictException(String cause) {
        super(HttpStatus.CONFLICT, "Appointment conflicted: " + cause);
    }
}
