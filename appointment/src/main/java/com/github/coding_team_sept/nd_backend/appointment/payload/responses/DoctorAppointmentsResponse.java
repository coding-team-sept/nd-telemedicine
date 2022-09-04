package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

import java.util.List;

public record DoctorAppointmentsResponse(
        List<AppointmentResponse> appointments,
        List<AppUserResponse> patients
) {
    public record AppointmentResponse(
            Long id,
            Long patientId,
            String datetime
    ) {
    }
}
