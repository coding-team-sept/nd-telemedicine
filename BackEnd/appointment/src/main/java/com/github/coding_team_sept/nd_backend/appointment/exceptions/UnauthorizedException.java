package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException{
    public UnauthorizedException(String reason) {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized: " + reason);
    }
}
