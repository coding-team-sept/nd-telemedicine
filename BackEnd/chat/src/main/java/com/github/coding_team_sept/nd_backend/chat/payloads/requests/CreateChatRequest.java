package com.github.coding_team_sept.nd_backend.chat.payloads.requests;

public record CreateChatRequest(
        Long appointmentId,
        Long patientId,
        Long doctorId
) {
}
