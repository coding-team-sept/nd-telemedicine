package com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.DataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse extends DataResponse {
    public final Long id;
    public final UserDataResponse appointedUser;
    public final String datetime;
    public final String session;
    public final ChatResponse chatStatus;

    public AppointmentResponse() {
        this(null, null, null, null, null);
    }

    public AppointmentResponse(
            Long id,
            UserDataResponse appointedUser,
            String datetime,
            String session,
            ChatResponse chatStatus
    ) {
        this.id = id;
        this.appointedUser = appointedUser;
        this.datetime = datetime;
        this.session = session;
        this.chatStatus = chatStatus;
    }

    public UserDataResponse getAppointedUser() {
        return appointedUser;
    }
}
