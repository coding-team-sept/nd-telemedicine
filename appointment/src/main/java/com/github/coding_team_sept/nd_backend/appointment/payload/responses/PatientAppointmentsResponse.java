package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

import java.util.List;

public record PatientAppointmentsResponse(
        List<AppointmentResponse> appointments,
        List<AppUserResponse> doctors
) {
    public record AppointmentResponse(
            Long id,
            Long doctorId,
            String datetime
    ) {
    }
}
