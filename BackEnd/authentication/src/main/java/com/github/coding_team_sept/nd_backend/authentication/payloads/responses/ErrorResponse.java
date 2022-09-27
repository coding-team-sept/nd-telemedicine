package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

public class ErrorResponse extends AppResponse {
    public final String message;

    ErrorResponse() {
        this(null);
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse build(String message) {
        return new ErrorResponse(message);
    }

    public static ErrorResponse fromUnknownException(Exception e) {
        return ErrorResponse.build("Unknown Error: " + e.getMessage());
    }

    public static <T extends Exception> ErrorResponse fromException(T e) {
        return new ErrorResponse(e.getMessage());
    }

    public static ErrorResponse fromException(AppException e) {
        return new ErrorResponse(e.message);
    }
}
