package com.github.coding_team_sept.nd_backend.authentication;

import com.github.coding_team_sept.nd_backend.authentication.models.User;
import com.github.coding_team_sept.nd_backend.authentication.models.UserRegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService() {
    public void register(UserRegistrationRequest request) {
        final var user = User.builder()
                .email(request.email())
                .name(request.name())
                .password(request.password())
                .build();

        // TODO: Validate email
        // TODO: Validate name
        // TODO: Validate password

        // TODO: Store data to DB
    }
}
