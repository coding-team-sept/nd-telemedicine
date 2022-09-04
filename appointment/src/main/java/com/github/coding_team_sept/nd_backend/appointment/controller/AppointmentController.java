package com.github.coding_team_sept.nd_backend.appointment.controller;

import com.github.coding_team_sept.nd_backend.appointment.payload.responses.DoctorResponse;
import com.github.coding_team_sept.nd_backend.appointment.service.AppointmentService;
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
    ResponseEntity<List<DoctorResponse>> getAvailableDoctor(@PathVariable String datetime) {
        try {
            return new ResponseEntity<>(appointmentService.getAvailableDoctor(datetime), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/appointment")
    String getAppointment() {
        return appointmentService.getAppointment();
    }

    @PostMapping("/appointment")
    String addAppointment() {
        return appointmentService.addAppointment();
    }
}