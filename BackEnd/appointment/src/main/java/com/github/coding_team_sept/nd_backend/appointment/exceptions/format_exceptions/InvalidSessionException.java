package com.github.coding_team_sept.nd_backend.appointment.exceptions.format_exceptions;

public class InvalidSessionException extends DataFormatException {
    public InvalidSessionException() {
        super("session", "Session type is not recognized");
    }

    public InvalidSessionException(String cause) {
        super("session", "Session type (" + cause + ") is not recognized");
    }
}
