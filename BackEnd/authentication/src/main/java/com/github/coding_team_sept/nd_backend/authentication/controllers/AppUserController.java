package com.github.coding_team_sept.nd_backend.authentication.controllers;

import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.DataResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
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
    private ResponseEntity<ResponseWrapper<DataResponse>> addUser(RegisterRequest request, RoleType roleType) {
        service.register(request, roleType);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/admin/admin")
    public ResponseEntity<ResponseWrapper<DataResponse>> addAdmin(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_ADMIN);
    }

    @PostMapping("/admin/doctor")
    public ResponseEntity<ResponseWrapper<DataResponse>> addDoctor(@RequestBody RegisterRequest request) {
        return addUser(request, RoleType.ROLE_DOCTOR);
    }

    @GetMapping("/admin/admin")
    public ResponseEntity<ResponseWrapper<UsersDataResponse>> getAdmin() {
        return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUserByRole(RoleType.ROLE_ADMIN))));
    }

    @GetMapping("/admin/doctor")
    public ResponseEntity<ResponseWrapper<DataResponse>> getDoctor(
            @RequestParam(required = false) List<Long> ids
    ) {
        if (ids != null) {
            return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUsersByIds(ids, RoleType.ROLE_DOCTOR))));
        }
        return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUserByRole(RoleType.ROLE_DOCTOR))));
    }

    @GetMapping("/admin/patient")
    public ResponseEntity<ResponseWrapper<DataResponse>> getPatient(
            @RequestParam(required = false) List<Long> ids
    ) {
        if (ids != null) {
            return ResponseEntity.ok(ResponseWrapper.fromData(UsersDataResponse.build(appUserService.getUsersByIds(ids, RoleType.ROLE_PATIENT))));
        }
        return ResponseEntity.badRequest().build();
    }
}
