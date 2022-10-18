package com.github.coding_team_sept.nd_backend.chat.payloads.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessagesResponse extends DataResponse {
    final public List<MessageResponse> messages;

    public MessagesResponse() {
        this(null);
    }

    public MessagesResponse(List<MessageResponse> messages) {
        this.messages = messages;
    }

    public static MessagesResponse fromList(List<MessageResponse> messages) {
        return new MessagesResponse(messages);
    }

    @JsonCreator
    public List<MessageResponse> getMessages() {
        return messages;
    }
}
