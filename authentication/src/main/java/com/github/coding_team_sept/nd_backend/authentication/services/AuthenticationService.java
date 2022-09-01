package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public record AuthenticationService(
        AppUserRepository authenticationRepo,
        RoleRepository roleRepo,
        PasswordEncoder encoder,
        JwtUtils jwtUtils,
        AuthenticationManager authenticationManager,
        AppUserDetailsService userDetailsService
) {
    public String login(LoginRequest request) {
        final var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(), request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final var userDetails = userDetailsService.loadUserByEmail(request.email());
        return jwtUtils.generateToken(userDetails);
    }

    public String register(RegisterRequest request, RoleType roleType) throws DataIntegrityViolationException {
        // TODO: Create findBy for email. Source: https://stackoverflow.com/a/27583544

        // TODO: Validate email
        // TODO: Validate name
        // TODO: Validate password

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
        final var userDetails = new User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
        return jwtUtils.generateToken(userDetails);
    }
}
