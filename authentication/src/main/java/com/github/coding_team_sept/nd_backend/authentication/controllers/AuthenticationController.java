package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public record AuthenticationController(AuthenticationService service) {
    @GetMapping("/validate")
    public String validate() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/login")
    public ResponseEntity<AppResponse> login(@RequestBody LoginRequest request) {
        try {
            final var loginResponse = service.login(request);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error("Unknown error: " + e.getMessage(), e));
        }
    }

    @PostMapping("/admin/admin")
    public ResponseEntity<AppResponse> addAdmin(@RequestBody RegisterRequest request) {
        return register(request, RoleType.ROLE_ADMIN);
    }

    @PostMapping("/admin/doctor")
    public ResponseEntity<AppResponse> addDoctor(@RequestBody RegisterRequest request) {
        return register(request, RoleType.ROLE_DOCTOR);
    }

    @PostMapping("/register")
    public ResponseEntity<AppResponse> register(@RequestBody RegisterRequest request) {
        return register(request, RoleType.ROLE_PATIENT);
    }

    private ResponseEntity<AppResponse> register(RegisterRequest request, RoleType roleType) {
        try {
            final var response = service.register(request, roleType);
            if (roleType.equals(RoleType.ROLE_PATIENT)) {
                return new ResponseEntity<>(
                        response,
                        HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body(AppResponse.error("Email has been taken!", e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error("Unknown error: " + e.getMessage(), e));
        }
    }
}
