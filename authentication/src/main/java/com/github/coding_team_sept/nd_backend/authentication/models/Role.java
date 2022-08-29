package com.github.coding_team_sept.nd_backend.authentication.models;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @SequenceGenerator(
            name = "role_sequence_id",
            sequenceName = "role_sequence_id"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence_id"
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

}
