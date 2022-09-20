package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.EmailTakenException;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AuthResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.TokenResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import com.github.coding_team_sept.nd_backend.authentication.utils.ValidationUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record AuthenticationService(
        AppUserRepository authenticationRepo,
        RoleRepository roleRepo,
        PasswordEncoder encoder,
        JwtUtils jwtUtils,
        AuthenticationManager authenticationManager,
        AppUserDetailsService userDetailsService
) {
    public AppResponse login(LoginRequest request) throws AppException {
        // Validations
        ValidationUtils.validateEmailElseThrow(request.email());
        ValidationUtils.validatePasswordElseThrow(request.password());

        // Authenticate
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var userDetails = (AppUserDetails) authentication.getPrincipal();
        final var jwt = jwtUtils.generateToken(userDetails);
        return AppResponse.auth(AuthResponse.build(TokenResponse.build(jwt), UserDataResponse.fromUserDetails(userDetails)));
    }

    public AppResponse register(RegisterRequest request, RoleType roleType) throws AppException {
        // Check if email is used
        // Source: https://stackoverflow.com/a/27583544
        if (authenticationRepo.existsAppUserByEmail(request.email())) {
            throw new EmailTakenException();
        }

        // Validations
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
        return AppResponse.auth(AuthResponse.token(TokenResponse.build(jwt)));
    }
}
