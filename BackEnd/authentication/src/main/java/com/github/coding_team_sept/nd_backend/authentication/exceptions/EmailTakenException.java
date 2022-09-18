package com.github.coding_team_sept.nd_backend.authentication.exceptions;

public class EmailTakenException extends AppException {
    public EmailTakenException() {
        super(500, "Email has been taken");
    }
}
