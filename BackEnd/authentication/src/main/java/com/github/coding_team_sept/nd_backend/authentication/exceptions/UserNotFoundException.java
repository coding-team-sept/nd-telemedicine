package com.github.coding_team_sept.nd_backend.authentication.exceptions;

public class UserNotFoundException extends AppException{
    public UserNotFoundException() {
        super(500, "User not found");
    }
}
