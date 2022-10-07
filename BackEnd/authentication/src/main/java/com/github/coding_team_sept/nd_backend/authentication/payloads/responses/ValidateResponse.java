package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;

public class ValidateResponse {
    public final Long id;
    public final String role;

    public ValidateResponse() {
        this(null, null);
    }

    public ValidateResponse(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    public static ValidateResponse fromRoleType (Long id, RoleType role) {
        return new ValidateResponse(id, role.name());
    }

    public static ValidateResponse fromRole(Long id, Role role) {
        return fromRoleType(id, role.getName());
    }
}
