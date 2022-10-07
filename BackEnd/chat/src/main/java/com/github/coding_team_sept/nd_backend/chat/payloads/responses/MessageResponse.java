package com.github.coding_team_sept.nd_backend.chat.payloads.responses;

public record MessageResponse(
        Long senderId,
        String message
) {
}
