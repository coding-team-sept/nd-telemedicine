package com.github.coding_team_sept.nd_backend.appointment.payload.responses.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UserDataResponse;

public class DoctorAppointmentResponse extends AppointmentResponse {
    @JsonProperty("patient")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }

    public DoctorAppointmentResponse() {
        this(null, null, null);
    }

    public DoctorAppointmentResponse(Long id, UserDataResponse patient, String datetime) {
        super(id, patient, datetime);
    }

    public static DoctorAppointmentResponse build(
            Long id,
            UserDataResponse patient,
            String datetime
    ) {
        return new DoctorAppointmentResponse(id, patient, datetime);
    }
}
