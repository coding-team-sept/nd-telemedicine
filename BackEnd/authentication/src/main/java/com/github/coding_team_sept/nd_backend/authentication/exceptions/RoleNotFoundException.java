package com.github.coding_team_sept.nd_backend.authentication.exceptions;

public class RoleNotFoundException extends AppException{
    public RoleNotFoundException() {
        super(404, "Role not found");
    }
}
