package com.github.coding_team_sept.nd_backend.authentication.models;

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
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @ManyToOne(
            targetEntity = Role.class,
            cascade = CascadeType.ALL,
            optional = false
    )
    private Role role;
}
