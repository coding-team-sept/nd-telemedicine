package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

public record PatientAppointmentResponse(
        Long id,
        AppUserResponse doctor,
        String datetime
) {
}
