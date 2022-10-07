package com.github.coding_team_sept.nd_backend.chat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    private Long appointmentId;
    private Long patientId;
    private Long doctorId;
    private Long patientRR;
    private Long doctorRR;
    private Long messageCount;
}
