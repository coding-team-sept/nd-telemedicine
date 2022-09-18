package com.github.coding_team_sept.nd_backend.authentication.payloads.requests;

public record LoginRequest(
        String email,
        String password
) {
}
