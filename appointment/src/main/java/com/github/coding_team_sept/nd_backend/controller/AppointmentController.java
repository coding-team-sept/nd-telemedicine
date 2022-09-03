package com.github.coding_team_sept.nd_backend.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("api/v1")
public record AppointmentController() {
    @GetMapping("/doctor")
    public String getDoctor(@RequestParam ("date")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {

        // ...
    }
    @PostMapping("/localdate")
    public void localDate(@RequestParam("localDate")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        // ...
    }
    @PostMapping("/localdatetime")
    public void dateTime(@RequestParam("localDateTime")
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        // ...

    }
    @PostMapping("/date")
    public void date(@RequestParam("date")
                     @DateTimeFormat(pattern = "yyyy.MM.dd") Date date) {
        // ...
    }
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

