package com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientAppointmentResponse extends AppointmentResponse {
    public PatientAppointmentResponse() {
        this(null, null, null, null, null);
    }

    public PatientAppointmentResponse(
            Long id,
            UserDataResponse doctor,
            String datetime,
            String session,
            ChatResponse chatStatus
    ) {
        super(id, doctor, datetime, session, chatStatus);
    }

    public static PatientAppointmentResponse build(
            Long id,
            UserDataResponse doctor,
            String datetime,
            SessionType session,
            ChatResponse chatStatus
    ) {
        return new PatientAppointmentResponse(
                id,
                doctor,
                datetime,
                session.name(),
                !session.equals(SessionType.ONLINE) ? null : chatStatus
        );
    }

    @JsonProperty("doctor")
    @Override
    public UserDataResponse getAppointedUser() {
        return super.getAppointedUser();
    }
}
