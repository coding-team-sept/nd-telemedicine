package com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorAppointmentResponse extends AppointmentResponse {
    public DoctorAppointmentResponse() {
        this(null, null, null, null, null);
    }

    public DoctorAppointmentResponse(
            Long id,
            UserDataResponse patient,
            String datetime,
            String session,
            ChatResponse chatStatus
    ) {
        super(id, patient, datetime, session, chatStatus);
    }

    public static DoctorAppointmentResponse build(
            Long id,
            UserDataResponse patient,
            String datetime,
            SessionType session,
            ChatResponse chatStatus
    ) {
        return new DoctorAppointmentResponse(
                id,
                patient,
                datetime,
                session.name(),
                !session.equals(SessionType.ONLINE) ? null : chatStatus
        );
    }

    @JsonProperty("patient")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }
}
