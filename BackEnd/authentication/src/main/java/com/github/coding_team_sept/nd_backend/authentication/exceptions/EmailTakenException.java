package com.github.coding_team_sept.nd_backend.authentication.exceptions;

import org.springframework.http.HttpStatus;

public class EmailTakenException extends AppException {
    public EmailTakenException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Email has been taken");
    }
}
