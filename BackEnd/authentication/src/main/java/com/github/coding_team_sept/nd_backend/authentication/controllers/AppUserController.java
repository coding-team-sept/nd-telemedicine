package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppUserResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/app")
public record AppUserController(
        AuthenticationService service,
        AppUserService appUserService
) {
    private ResponseEntity<AppResponse> register(RegisterRequest request, RoleType roleType) {
        try {
            service.register(request, roleType);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body(AppResponse.error("Email has been taken!", e));
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

    @GetMapping("/admin/admin")
    public ResponseEntity<List<AppUserResponse>> getAdmin() {
        try {
            return ResponseEntity.ok(appUserService.getUserByRole(RoleType.ROLE_ADMIN));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin/doctor")
    public ResponseEntity<List<AppUserResponse>> getDoctor(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) List<Long> ids
    ) {
        try {
            if (id != null) {
                return ResponseEntity.ok(List.of(appUserService.getUserById(id, RoleType.ROLE_DOCTOR)));
            } else if (ids != null) {
                return ResponseEntity.ok(appUserService.getUsersByIds(ids, RoleType.ROLE_DOCTOR));
            }
            return ResponseEntity.ok(appUserService.getUserByRole(RoleType.ROLE_DOCTOR));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/admin/patient")
    public ResponseEntity<List<AppUserResponse>> getPatient(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) List<Long> ids) {
        try {
            if (id != null) {
                return ResponseEntity.ok(List.of(appUserService.getUserById(id, RoleType.ROLE_PATIENT)));
            } else if (ids != null) {
                return ResponseEntity.ok(appUserService.getUsersByIds(ids, RoleType.ROLE_PATIENT));
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
