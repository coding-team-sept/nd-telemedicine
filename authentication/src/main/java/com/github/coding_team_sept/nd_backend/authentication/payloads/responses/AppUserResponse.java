package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

public record AppUserResponse(
        Long id,
        String email,
        String name
) {
}
