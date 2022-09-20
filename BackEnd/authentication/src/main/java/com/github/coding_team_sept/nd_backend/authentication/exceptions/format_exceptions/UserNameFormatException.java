package com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

public class UserNameFormatException extends DataFormatException {
    public UserNameFormatException(String reason) {
        super("name", reason);
    }
}
