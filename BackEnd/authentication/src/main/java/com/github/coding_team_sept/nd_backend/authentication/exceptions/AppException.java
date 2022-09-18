package com.github.coding_team_sept.nd_backend.authentication.exceptions;

public class AppException extends RuntimeException{
    public final int status;
    public final String message;

    public AppException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
