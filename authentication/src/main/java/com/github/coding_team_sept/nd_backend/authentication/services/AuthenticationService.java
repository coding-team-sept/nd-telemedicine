package com.github.coding_team_sept.nd_backend.authentication.services;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    public AppResponse register(RegisterRequest request, RoleType roleType) throws DataIntegrityViolationException {
        // TODO: Create findBy for email. Source: https://stackoverflow.com/a/27583544

        if (Pattern.compile("^(.+)@(\\S+)$").matcher(request.email()).matches()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        if (Pattern.compile("^[A-Za-z_][A-Za-z0-9_]{7,29}$").matcher(request.name()).matches()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid name");
        }
        if (request.password().length() < 8){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid Password");
        }

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
