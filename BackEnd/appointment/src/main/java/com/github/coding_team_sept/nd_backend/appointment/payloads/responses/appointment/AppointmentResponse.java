package com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment;

import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.DataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;

public class AppointmentResponse extends DataResponse {
    public final Long id;
    public final UserDataResponse appointedUser;
    public final String datetime;

    public UserDataResponse getAppointedUser() {
        return appointedUser;
    }

    public AppointmentResponse() {
        this(null, null, null);
    }

    public AppointmentResponse(Long id, UserDataResponse appointedUser, String datetime) {
        this.id = id;
        this.appointedUser = appointedUser;
        this.datetime = datetime;
    }
}
