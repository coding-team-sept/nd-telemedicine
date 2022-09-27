package com.github.coding_team_sept.nd_backend.appointment.payload.responses;

import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;

public class PatientAppointmentResponse extends DataResponse {
    public final Long id;
    public final UserDataResponse doctor;
    public final String datetime;

    public PatientAppointmentResponse() {
        this(null, null, null);
    }

    public PatientAppointmentResponse(Long id, UserDataResponse doctor, String datetime) {
        this.id = id;
        this.doctor = doctor;
        this.datetime = datetime;
    }

    public static PatientAppointmentResponse build(
            Long id,
            UserDataResponse doctor,
            String datetime
    ) {
        return new PatientAppointmentResponse(id, doctor, datetime);
    }
}
