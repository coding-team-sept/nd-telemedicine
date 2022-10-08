package com.github.coding_team_sept.nd_backend.chat.exceptions;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    public final HttpStatus status;
    public final String message;

    public AppException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
