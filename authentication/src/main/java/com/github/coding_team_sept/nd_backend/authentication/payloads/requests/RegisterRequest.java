package com.github.coding_team_sept.nd_backend.authentication.payloads.requests;

public record RegisterRequest(
        String email,
        String name,
        String password
) {
}
