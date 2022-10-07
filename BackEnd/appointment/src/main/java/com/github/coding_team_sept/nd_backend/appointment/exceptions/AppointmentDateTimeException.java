package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import org.springframework.http.HttpStatus;

public class AppointmentDateTimeException extends AppException{
    public AppointmentDateTimeException(String reason) {
        super(HttpStatus.BAD_REQUEST, "Invalid datetime: " + reason);
    }
}
