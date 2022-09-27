package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

public record DoctorAppointmentResponse(
        Long id,
        UserDataResponse patient,
        String datetime
) {
}
