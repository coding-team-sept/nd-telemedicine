package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ErrorResponse;
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
    private ResponseEntity<AppResponse> addUser(RegisterRequest request, RoleType roleType) {
        try {
            service.register(request, roleType);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body(AppResponse.error(ErrorResponse.build("Email has been taken!")));
        } catch (AppException e) {
            return new ResponseEntity<>(AppResponse.error(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error(ErrorResponse.fromUnknownException(e)));
        }
    }

    @PostMapping("/admin/admin")
    public ResponseEntity<AppResponse> addAdmin(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_ADMIN);
    }

    @PostMapping("/admin/doctor")
    public ResponseEntity<AppResponse> addDoctor(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_DOCTOR);
    }

    @GetMapping("/admin/admin")
    public ResponseEntity<AppResponse> getAdmin() {
        try {
            return ResponseEntity.ok(AppResponse.user(appUserService.getUserByRole(RoleType.ROLE_ADMIN)));
        } catch (AppException e) {
            return new ResponseEntity<>(AppResponse.error(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error(ErrorResponse.fromUnknownException(e)));
        }
    }

    @GetMapping("/admin/doctor")
    public ResponseEntity<AppResponse> getDoctor(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) List<Long> ids
    ) {
        try {
            if (id != null) {
                return ResponseEntity.ok(AppResponse.user(appUserService.getUserById(id, RoleType.ROLE_DOCTOR)));
            } else if (ids != null) {
                return ResponseEntity.ok(AppResponse.user(appUserService.getUsersByIds(ids, RoleType.ROLE_DOCTOR)));
            }
            return ResponseEntity.ok(AppResponse.user(appUserService.getUserByRole(RoleType.ROLE_DOCTOR)));
        } catch (AppException e) {
            return new ResponseEntity<>(AppResponse.error(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error(ErrorResponse.fromUnknownException(e)));
        }
    }

    @GetMapping("/admin/patient")
    public ResponseEntity<AppResponse> getPatient(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) List<Long> ids) {
        try {
            if (id != null) {
                return ResponseEntity.ok(AppResponse.user(appUserService.getUserById(id, RoleType.ROLE_PATIENT)));
            } else if (ids != null) {
                return ResponseEntity.ok(AppResponse.user(appUserService.getUsersByIds(ids, RoleType.ROLE_PATIENT)));
            }
            return ResponseEntity.badRequest().build();
        } catch (AppException e) {
            return new ResponseEntity<>(AppResponse.error(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(AppResponse.error(ErrorResponse.fromUnknownException(e)));
        }
    }
}
