package com.github.coding_team_sept.nd_backend.authentication.models;

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
public class AppUser {
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
    private String email;
    private String name;
    private String password;
}
