package com.github.coding_team_sept.nd_backend.appointment.payload.responses.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UserDataResponse;

public class PatientAppointmentResponse extends AppointmentResponse {
    @JsonProperty("doctor")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }

    public PatientAppointmentResponse() {
        this(null, null, null);
    }

    public PatientAppointmentResponse(Long id, UserDataResponse doctor, String datetime) {
        super(id, doctor, datetime);
    }

    public static PatientAppointmentResponse build(
            Long id,
            UserDataResponse doctor,
            String datetime
    ) {
        return new PatientAppointmentResponse(id, doctor, datetime);
    }
}
