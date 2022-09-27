package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

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
}
