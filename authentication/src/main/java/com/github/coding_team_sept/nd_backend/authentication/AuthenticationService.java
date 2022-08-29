package com.github.coding_team_sept.nd_backend.authentication;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserRegistrationRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService(AuthenticationRepository repository) {
    public void register(AppUserRegistrationRequest request) {
        // TODO: Set response to 201 ("Created") for successfully registered user
        // TODO: Create findBy for email. Source: https://stackoverflow.com/a/27583544

        final var appUser = AppUser.builder()
                .email(request.email())
                .name(request.name())
                .password(request.password())
                .build();

        // TODO: Validate email
        // TODO: Validate name
        // TODO: Validate password

        try {
            repository.save(appUser);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Email has been taken!");
            throw new DataIntegrityViolationException("Email has been taken!");
        }
    }
}
