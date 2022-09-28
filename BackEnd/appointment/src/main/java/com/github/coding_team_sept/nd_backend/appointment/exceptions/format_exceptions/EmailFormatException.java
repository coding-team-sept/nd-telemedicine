package com.github.coding_team_sept.nd_backend.appointment.exceptions.format_exceptions;

public class EmailFormatException extends DataFormatException {
    public EmailFormatException(String reason) {
        super("email", reason);
    }
}
