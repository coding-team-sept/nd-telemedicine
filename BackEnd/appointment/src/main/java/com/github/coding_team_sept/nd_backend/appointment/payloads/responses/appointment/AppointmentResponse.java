package com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment;

import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.DataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;

public class AppointmentResponse extends DataResponse {
    public final Long id;
    public final UserDataResponse appointedUser;
    public final String datetime;
    public final String session;

    public AppointmentResponse() {
        this(null, null, null, null);
    }

    public AppointmentResponse(Long id, UserDataResponse appointedUser, String datetime, String session) {
        this.id = id;
        this.appointedUser = appointedUser;
        this.datetime = datetime;
        this.session = session;
    }

    public UserDataResponse getAppointedUser() {
        return appointedUser;
    }
}
