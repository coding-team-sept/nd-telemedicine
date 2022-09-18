package com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

public class DataFormatException extends AppException {
    public DataFormatException(String subject, String reason) {
        super(500, "Invalid " + subject.toLowerCase() + " format: " + reason);
    }
}
