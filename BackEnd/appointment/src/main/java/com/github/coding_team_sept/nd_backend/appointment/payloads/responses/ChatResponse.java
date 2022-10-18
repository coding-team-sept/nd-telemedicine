package com.github.coding_team_sept.nd_backend.appointment.payloads.responses;

public class ChatResponse extends DataResponse {
    public final Integer patientUR;
    public final Integer doctorUR;

    public ChatResponse() {
        this(null, null);
    }

    public ChatResponse(Integer patientUR, Integer doctorUR) {
        this.patientUR = patientUR;
        this.doctorUR = doctorUR;
    }
}
