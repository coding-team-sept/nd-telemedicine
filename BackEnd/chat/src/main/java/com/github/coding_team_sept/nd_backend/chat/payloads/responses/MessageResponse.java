package com.github.coding_team_sept.nd_backend.chat.payloads.responses;

public class MessageResponse extends DataResponse {
    public final Long id;
    public final Long senderId;
    public final String message;

    public MessageResponse() {
        this(null, null, null);
    }

    public MessageResponse(Long id, Long senderId, String message) {
        this.id = id;
        this.senderId = senderId;
        this.message = message;
    }
}
