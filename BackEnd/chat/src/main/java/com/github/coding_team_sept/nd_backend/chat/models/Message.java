package com.github.coding_team_sept.nd_backend.chat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @SequenceGenerator(
            name = "chat_sequence_id",
            sequenceName = "chat_sequence_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_sequence_id"
    )
    private Long id;
    private Long appointmentId;
    private Long senderId;
    private String message;
}
