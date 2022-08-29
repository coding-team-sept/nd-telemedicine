package com.github.coding_team_sept.nd_backend.authentication;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserRegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService(AuthenticationRepository repository) {
    public void register(AppUserRegistrationRequest request) {
        final var user = AppUser.builder()
                .email(request.email())
                .name(request.name())
                .password(request.password())
                .build();

        // TODO: Validate email
        // TODO: Validate name
        // TODO: Validate password

        repository.save(user);
    }
}
