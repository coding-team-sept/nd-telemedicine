package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AuthResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ValidateResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public record AuthenticationController(AuthenticationService service, AppUserService appUserService) {
    @GetMapping("/validate")
    public ValidateResponse validate() {
        final var authentication = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ValidateResponse.fromRole(authentication.getId(), authentication.getRole());
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper<AuthResponse>> login(@RequestBody LoginRequest request) {
        final var loginResponse = service.login(request);
        return ResponseEntity.ok(ResponseWrapper.fromData(loginResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<AuthResponse>> register(@RequestBody RegisterRequest request) {
        final var response = service.register(request, RoleType.ROLE_PATIENT);
        return new ResponseEntity<>(ResponseWrapper.fromData(response), HttpStatus.CREATED);
    }
}
