package com.github.coding_team_sept.nd_backend.authentication.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String name;
    private String password;
}
