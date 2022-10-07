package com.github.coding_team_sept.nd_backend.appointment.models;

import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSession {
    @Id
    @SequenceGenerator(
            name = "session_sequence_id",
            sequenceName = "session_sequence_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "session_sequence_id"
    )
    private Long id;
    @Enumerated(EnumType.STRING)
    private SessionType name;
}