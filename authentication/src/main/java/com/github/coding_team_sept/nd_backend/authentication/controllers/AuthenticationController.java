package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
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
    @GetMapping("/validate")
    public String validate() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @PostMapping("/admin/admin")
    public ResponseEntity<AppUserResponse> addAdmin(@RequestBody AppUserRegistrationRequest request) {
        return register(request, RoleType.ROLE_ADMIN);
    }

    @PostMapping("/admin/doctor")
    public ResponseEntity<AppUserResponse> addDoctor(@RequestBody AppUserRegistrationRequest request) {
        return register(request, RoleType.ROLE_DOCTOR);
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserResponse> register(@RequestBody AppUserRegistrationRequest request) {
        return register(request, RoleType.ROLE_PATIENT);
    }

    private ResponseEntity<AppUserResponse> register(AppUserRegistrationRequest request, RoleType roleType) {
        try {
            final var token = service.register(request, roleType);
            return new ResponseEntity<>(
                    new AppUserResponse(token, null),
                    HttpStatus.CREATED
            );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body(new AppUserResponse(null, "Email has been taken!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AppUserResponse(null, "Unknown error: " + e.getMessage()));
        }
    }
}
