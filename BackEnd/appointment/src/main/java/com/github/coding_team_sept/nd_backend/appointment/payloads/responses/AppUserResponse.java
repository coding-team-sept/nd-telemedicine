package com.github.coding_team_sept.nd_backend.appointment.payloads.responses;

public record AppUserResponse(
        Long id,
        String email,
        String name
) {
}
