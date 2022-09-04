package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppUserResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public record AppUserController(
        AuthenticationService service,
        AppUserService appUserService
) {
    @GetMapping("/doctor")
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

    @GetMapping("/patient")
    public ResponseEntity<AppUserResponse> getPatient(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(appUserService.getUserById(id, RoleType.ROLE_PATIENT));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
