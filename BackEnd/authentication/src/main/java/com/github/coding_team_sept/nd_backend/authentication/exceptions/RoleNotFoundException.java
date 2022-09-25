package com.github.coding_team_sept.nd_backend.authentication.exceptions;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends AppException{
    public RoleNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Role not found");
    }
}
