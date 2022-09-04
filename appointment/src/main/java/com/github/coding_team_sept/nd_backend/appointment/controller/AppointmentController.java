package com.github.coding_team_sept.nd_backend.appointment.controller;

import com.github.coding_team_sept.nd_backend.appointment.payload.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.AppUserResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.AppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.service.AppointmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public record AppointmentController(
        AppointmentService appointmentService
) {
    @GetMapping("/available/{datetime}")
    ResponseEntity<List<AppUserResponse>> getAvailableDoctor(@PathVariable String datetime) {
        try {
            return new ResponseEntity<>(appointmentService.getAvailableDoctor(datetime), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/appointment")
    ResponseEntity<AppointmentResponse> addAppointment(@RequestHeader HttpHeaders headers, @RequestBody AppointmentRequest body) {
        return ResponseEntity.ok(appointmentService.addAppointment(headers, body));
    }

    @GetMapping("/appointment")
    String getAppointment() {
        return appointmentService.getAppointment();
    }
}