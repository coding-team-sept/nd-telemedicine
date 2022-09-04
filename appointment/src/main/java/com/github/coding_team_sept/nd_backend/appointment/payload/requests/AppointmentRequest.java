package com.github.coding_team_sept.nd_backend.appointment.payload.requests;

public record AppointmentRequest(
        Long doctorId,
        String datetime
) {
}
