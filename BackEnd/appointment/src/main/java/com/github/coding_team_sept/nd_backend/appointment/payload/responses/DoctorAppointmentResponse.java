package com.github.coding_team_sept.nd_backend.appointment.payload.responses;


public class DoctorAppointmentResponse extends DataResponse {
    public final Long id;
    public final UserDataResponse patient;
    public final String datetime;

    public DoctorAppointmentResponse() {
        this(null, null, null);
    }

    public DoctorAppointmentResponse(Long id, UserDataResponse patient, String datetime) {
        this.id = id;
        this.patient = patient;
        this.datetime = datetime;
    }

    public static DoctorAppointmentResponse build(
            Long id,
            UserDataResponse patient,
            String datetime
    ) {
        return new DoctorAppointmentResponse(id, patient, datetime);
    }
}
