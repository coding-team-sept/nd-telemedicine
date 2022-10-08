package com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.UserDataResponse;

public class DoctorAppointmentResponse extends AppointmentResponse {
    public DoctorAppointmentResponse() {
        this(null, null, null, null);
    }

    public DoctorAppointmentResponse(Long id, UserDataResponse patient, String datetime, String session) {
        super(id, patient, datetime, session);
    }

    @JsonProperty("patient")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }
}
