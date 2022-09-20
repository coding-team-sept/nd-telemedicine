package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;

public class ValidateResponse {
    public final Long id;
    public final String role;

    public ValidateResponse(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public ValidateResponse(Long id, RoleType role) {
        this(id, role.name());
    }

    public ValidateResponse(Long id, Role role) {
        this(id, role.getName());
    }
}
