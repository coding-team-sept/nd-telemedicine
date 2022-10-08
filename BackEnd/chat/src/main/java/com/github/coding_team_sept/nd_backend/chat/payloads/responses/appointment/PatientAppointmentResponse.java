package com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.UserDataResponse;

public class PatientAppointmentResponse extends AppointmentResponse {
    public PatientAppointmentResponse() {
        this(null, null, null, null);
    }

    public PatientAppointmentResponse(Long id, UserDataResponse doctor, String datetime, String session) {
        super(id, doctor, datetime, session);
    }

    @JsonProperty("doctor")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }
}
