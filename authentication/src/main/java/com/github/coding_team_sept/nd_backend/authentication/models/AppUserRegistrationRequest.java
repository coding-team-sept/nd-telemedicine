package com.github.coding_team_sept.nd_backend.authentication.models;

public record AppUserRegistrationRequest(
        String email,
        String name,
        String password
) {
}
