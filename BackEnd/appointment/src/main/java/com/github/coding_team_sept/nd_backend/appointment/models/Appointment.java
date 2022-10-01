package com.github.coding_team_sept.nd_backend.appointment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @SequenceGenerator(
            name = "user_sequence_id",
            sequenceName = "user_sequence_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence_id"
    )
    private Long id;
    @Column(nullable = false)
    private Long patientId;
    @Column(nullable = false)
    private Long doctorId;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentTime;
}
