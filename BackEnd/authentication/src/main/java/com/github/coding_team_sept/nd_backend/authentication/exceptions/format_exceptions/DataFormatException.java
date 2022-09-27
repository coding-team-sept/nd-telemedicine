package com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import org.springframework.http.HttpStatus;

public class DataFormatException extends AppException {
    public DataFormatException(String subject, String reason) {
        super(HttpStatus.BAD_REQUEST, "Invalid " + subject.toLowerCase() + " format: " + reason);
    }
}
