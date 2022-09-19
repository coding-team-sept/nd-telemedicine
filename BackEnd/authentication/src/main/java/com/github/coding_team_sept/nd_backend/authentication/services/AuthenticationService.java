package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.EmailTakenException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.EmailFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.PasswordFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.UserNameFormatException;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import com.github.coding_team_sept.nd_backend.authentication.utils.ValidationUtils;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public record AuthenticationService(
        AppUserRepository authenticationRepo,
        RoleRepository roleRepo,
        PasswordEncoder encoder,
        JwtUtils jwtUtils,
        AuthenticationManager authenticationManager,
        AppUserDetailsService userDetailsService
) {
    public AppResponse login(LoginRequest request) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var userDetails = (AppUserDetails) authentication.getPrincipal();
        final var jwt = jwtUtils.generateToken(userDetails);
        return AppResponse.login(jwt, userDetails);
    }

    public AppResponse register(RegisterRequest request, RoleType roleType) throws AppException {
        // Source: https://stackoverflow.com/a/27583544
        if (authenticationRepo.existsAppUserByEmail(request.email())) {
            throw new EmailTakenException();
        }

        ValidationUtils.validateEmailElseThrow(request.email());
        ValidationUtils.validateUserNameElseThrow(request.name());
        ValidationUtils.validatePasswordElseThrow(request.password());

        // Create model from request
        final var appUser = AppUser.builder()
                .email(request.email())
                .name(request.name())
                .password(encoder.encode(request.password()))
                .role(roleRepo.findRoleByName(roleType).orElse(null))
                .build();

        // Save data to DB
        authenticationRepo.save(appUser);

        // Generate jwt
        final var userDetails = AppUserDetails.fromAppUser(appUser);
        final var jwt = jwtUtils.generateToken(userDetails);
        return AppResponse.register(jwt);
    }
}
