package com.github.coding_team_sept.nd_backend.chat.payloads.requests;

public record MessageRequest(
        Long appointmentId,
        String message
) {
}
