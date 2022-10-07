package com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

public class PasswordFormatException extends DataFormatException {
    public PasswordFormatException(String reason) {
        super("password", reason);
    }
}
