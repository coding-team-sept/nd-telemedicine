package com.github.coding_team_sept.nd_backend.chat.services;

import com.github.coding_team_sept.nd_backend.chat.models.Chat;
import com.github.coding_team_sept.nd_backend.chat.models.Message;
import com.github.coding_team_sept.nd_backend.chat.payloads.requests.MessageRequest;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.MessageResponse;
import com.github.coding_team_sept.nd_backend.chat.repositories.ChatRepositories;
import com.github.coding_team_sept.nd_backend.chat.repositories.MessageRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public record ChatService(
        ChatRepositories chatRepo,
        MessageRepositories msgRepo,
        AuthenticationService authService
) {
    private void createChatIfNotExists(Long appointmentId, Long patientId, Long doctorId) {
        final var isChatExists = chatRepo.existsById(appointmentId);
        if (!isChatExists) {
            final var chat = Chat.builder()
                    .appointmentId(appointmentId)
                    .patientId(patientId)
                    .doctorId(doctorId) // TODO: Get appointment doctor
                    .patientUR(0)
                    .doctorUR(0)
                    .lastMessageSid(0L)
                    .build();
            chatRepo.save(chat);
        }
    }

    public ChatResponse getChatStatus(
            HttpHeaders headers,
            Long appointmentId
    ) throws Exception {
        final var auth = authService.getAuthorization(headers);
        if (auth.role.contains("doctor") || auth.role.contains("patient")) {
            // TODO: Check appointment id
            createChatIfNotExists(appointmentId, auth.id, 100L);
            return ChatResponse.fromChat(
                    chatRepo.findById(appointmentId)
                            .orElseThrow(() -> new Exception("Chat is not created"))
            );
        } else {
            throw new Exception("Unauthorized");
        }
    }

    public ChatResponse sendMessage(
            HttpHeaders headers,
            MessageRequest messageRequest
    ) throws Exception {
        final var auth = authService.getAuthorization(headers);
        final var chat = chatRepo.findById(messageRequest.appointmentId())
                .orElseThrow(() -> new Exception("Chat does not exists"));
        final var newSid = chat.getLastMessageSid() + 1;
        final var message = Message.builder()
                .sid(newSid)
                .appointmentId(messageRequest.appointmentId())
                .senderId(auth.id)
                .message(messageRequest.message())
                .build();
        msgRepo.save(message);
        if (auth.role.toLowerCase().contains("doctor")) {
            chat.setPatientUR(chat.getPatientUR() + 1);
        } else if (auth.role.toLowerCase().contains("patient")) {
            chat.setDoctorUR(chat.getDoctorUR() + 1);
        } else {
            throw new Exception("Role not found");
        }
        chat.setLastMessageSid(newSid);
        return ChatResponse.fromChat(chatRepo.saveAndFlush(chat));
    }

    private List<MessageResponse> getSortedMessages(List<Message> sortedMessages, Long sid) {
        return sortedMessages.stream()
                .filter(message -> message.getSid() > sid)
                .map(message -> new MessageResponse(
                        message.getId(),
                        message.getSenderId(),
                        message.getMessage()
                )).toList();
    }

    public List<MessageResponse> getMessages(
            HttpHeaders headers,
            Long appointmentId,
            boolean isAll
    ) throws Exception {
        final var auth = authService.getAuthorization(headers);
        final var chat = chatRepo.findById(appointmentId)
                .orElseThrow(() -> new Exception("Chat does not exist"));
        List<MessageResponse> messages = List.of();
        if (!isAll) {
            final var sortedMessages = msgRepo.findAllByAppointmentId(appointmentId).stream()
                    .sorted(Comparator.comparing(Message::getSid))
                    .toList();
            Long sid = chat.getLastMessageSid();
            if (auth.role.toLowerCase().contains("doctor")) {
                for (int i = sortedMessages.size() - 1, count = 0; i > 0 && count > chat.getDoctorUR(); i++) {
                    if (sortedMessages.get(i).getSenderId().equals(auth.id)) {
                        sid = sortedMessages.get(i).getSid();
                        count++;
                    }
                }
                return getSortedMessages(sortedMessages, sid);
            } else if (auth.role.toLowerCase().contains("patient")) {
                for (int i = sortedMessages.size() - 1, count = 0; i > 0 && count > chat.getPatientUR(); i++) {
                    if (sortedMessages.get(i).getSenderId().equals(auth.id)) {
                        sid = sortedMessages.get(i).getSid();
                        count++;
                    }
                }
                return getSortedMessages(sortedMessages, sid);
            } else {
                throw new Exception("Role not found");
            }
        } else {
            messages = msgRepo.findAllByAppointmentId(appointmentId).stream()
                    .sorted(Comparator.comparing(Message::getSid))
                    .map(message -> new MessageResponse(
                            message.getId(),
                            message.getSenderId(),
                            message.getMessage()
                    )).toList();
        }
        if (auth.role.toLowerCase().contains("doctor")) {
            chat.setDoctorUR(0);
        } else if (auth.role.toLowerCase().contains("patient")) {
            chat.setPatientUR(0);
        } else {
            throw new Exception("Role not found");
        }
        chatRepo.save(chat);
        return messages;
    }
}
