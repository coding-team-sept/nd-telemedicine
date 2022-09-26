package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.DataResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ErrorResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.UsersDataResponse;
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
    private ResponseEntity<ResponseWrapper<DataResponse, ErrorResponse>> addUser(RegisterRequest request, RoleType roleType) {
        try {
            service.register(request, roleType);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.fromError(ErrorResponse.build("Email has been taken!")));
        } catch (AppException e) {
            return new ResponseEntity<>(ResponseWrapper.fromError(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.fromError(ErrorResponse.fromUnknownException(e)));
        }
    }

    @PostMapping("/admin/admin")
    public ResponseEntity<ResponseWrapper<DataResponse, ErrorResponse>> addAdmin(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_ADMIN);
    }

    @PostMapping("/admin/doctor")
    public ResponseEntity<ResponseWrapper<DataResponse, ErrorResponse>> addDoctor(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_DOCTOR);
    }

    @GetMapping("/admin/admin")
    public ResponseEntity<ResponseWrapper<UsersDataResponse, ErrorResponse>> getAdmin() {
        try {
            return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUserByRole(RoleType.ROLE_ADMIN))));
        } catch (AppException e) {
            return new ResponseEntity<>(ResponseWrapper.fromError(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.fromError(ErrorResponse.fromUnknownException(e)));
        }
    }

    @GetMapping("/admin/doctor")
    public ResponseEntity<ResponseWrapper<DataResponse, ErrorResponse>> getDoctor(
            @RequestParam(required = false) List<Long> ids
    ) {
        try {
            if (ids != null) {
                return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUsersByIds(ids, RoleType.ROLE_DOCTOR))));
            }
            return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUserByRole(RoleType.ROLE_DOCTOR))));
        } catch (AppException e) {
            return new ResponseEntity<>(ResponseWrapper.fromError(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.fromError(ErrorResponse.fromUnknownException(e)));
        }
    }

    @GetMapping("/admin/patient")
    public ResponseEntity<ResponseWrapper<DataResponse, ErrorResponse>> getPatient(
            @RequestParam(required = false) List<Long> ids
    ) {
        try {
            if (ids != null) {
                return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUsersByIds(ids, RoleType.ROLE_PATIENT))));
            }
            return ResponseEntity.badRequest().build();
        } catch (AppException e) {
            return new ResponseEntity<>(ResponseWrapper.fromError(ErrorResponse.fromException(e)), e.status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseWrapper.fromError(ErrorResponse.fromUnknownException(e)));
        }
    }
}
