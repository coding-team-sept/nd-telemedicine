package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.AppUserRegistrationRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppUserResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public record AuthenticationController(AuthenticationService service) {

    @PostMapping("/register")
    public ResponseEntity<AppUserResponse> register(@RequestBody AppUserRegistrationRequest request) {
        try {
            final var token = service.register(request);
            return new ResponseEntity<>(
                    new AppUserResponse(token, null),
                    HttpStatus.CREATED
            );
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(
                    new AppUserResponse(null, "Email has been taken!"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new AppUserResponse(null, "Unknown error: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/validate")
    public String validate() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
