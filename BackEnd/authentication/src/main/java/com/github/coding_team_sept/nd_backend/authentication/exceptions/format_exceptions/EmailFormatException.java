package com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

public class EmailFormatException extends DataFormatException {
    public EmailFormatException(String reason) {
        super("email", reason);
    }
}
