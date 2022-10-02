package com.github.coding_team_sept.nd_backend.appointment.payloads.requests;

public record AppointmentRequest(
        Long doctorId,
        String datetime,
        String session
) {
}
