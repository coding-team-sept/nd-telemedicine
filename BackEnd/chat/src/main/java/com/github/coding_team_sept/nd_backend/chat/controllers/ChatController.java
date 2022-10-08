package com.github.coding_team_sept.nd_backend.chat.controllers;

import com.github.coding_team_sept.nd_backend.chat.payloads.requests.MessageRequest;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.MessagesResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.chat.services.ChatService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/app")
public record ChatController(
        ChatService chatService
) {
    @GetMapping("/chat/status/{appointmentId}")
    ResponseWrapper<ChatResponse> getChat(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long appointmentId
    ) throws Exception {
        final var response = chatService.getChatStatus(headers, appointmentId);
        return ResponseWrapper.fromData(response);
    }

    @PostMapping("/chat/message")
    ResponseWrapper<ChatResponse> sendMessage(
            @RequestHeader HttpHeaders headers,
            @RequestBody MessageRequest messageRequest
    ) throws Exception {
        final var response = chatService.sendMessage(headers, messageRequest);
        return ResponseWrapper.fromData(response);
    }

    @GetMapping("/chat/message/{appointmentId}")
    ResponseWrapper<MessagesResponse> getMessage(
            @RequestHeader HttpHeaders headers,
            @PathVariable Long appointmentId
    ) throws Exception {
        final var response = chatService.getMessages(headers, appointmentId, true);
        return ResponseWrapper.fromData(MessagesResponse.fromList(response));
    }
}
