package com.github.coding_team_sept.nd_backend.chat.payloads.responses;

public record ChatResponse(
        Long patientRR,
        Long doctorRR,
        Long messageCount
) {
}
