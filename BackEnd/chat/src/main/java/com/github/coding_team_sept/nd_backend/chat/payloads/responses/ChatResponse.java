package com.github.coding_team_sept.nd_backend.chat.payloads.responses;

import com.github.coding_team_sept.nd_backend.chat.models.Chat;

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

    public static ChatResponse fromChat(Chat chat) {
        return new ChatResponse(chat.getPatientUR(), chat.getDoctorUR());
    }
}
