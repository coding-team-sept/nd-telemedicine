package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;

public record ValidateResponse(
        Long id,
        String role
) {
}
