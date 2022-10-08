package com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.DataResponse;

import java.util.List;

public class AppointmentsResponse<T extends AppointmentResponse> extends DataResponse {
    public final List<T> appointments;

    public AppointmentsResponse() {
        this(null);
    }

    @JsonCreator
    public AppointmentsResponse(List<T> appointments) {
        this.appointments = appointments;
    }

    public static <T extends AppointmentResponse> AppointmentsResponse<T> build(List<T> appointments) {
        return new AppointmentsResponse<>(appointments);
    }

    @JsonValue
    public List<T> getAppointments() {
        return appointments;
    }
}
