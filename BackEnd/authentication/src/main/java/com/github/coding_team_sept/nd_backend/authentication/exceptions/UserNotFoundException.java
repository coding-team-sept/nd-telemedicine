package com.github.coding_team_sept.nd_backend.authentication.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException{
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found");
    }
}
