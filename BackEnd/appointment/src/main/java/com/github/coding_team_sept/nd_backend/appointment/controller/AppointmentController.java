package com.github.coding_team_sept.nd_backend.appointment.controller;

import com.github.coding_team_sept.nd_backend.appointment.payload.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.service.AppointmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public record AppointmentController(
        AppointmentService appointmentService
) {
    @GetMapping("/patient/doctor/{datetime}")
    ResponseEntity<ResponseWrapper<UsersDataResponse>> getAvailableDoctor(
            @RequestHeader HttpHeaders headers,
            @PathVariable String datetime
    ) {
        return ResponseEntity.ok(ResponseWrapper.fromData(appointmentService.getAvailableDoctor(headers, datetime)));
    }

    @PostMapping("/patient/appointment")
    ResponseEntity<PatientAppointmentResponse> addAppointment(
            @RequestHeader HttpHeaders headers,
            @RequestBody AppointmentRequest body
    ) {
        return ResponseEntity.ok(appointmentService.addAppointment(headers, body));
    }

    @GetMapping("/patient/appointment")
    ResponseEntity<List<PatientAppointmentResponse>> getPatientAppointment(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(appointmentService.getPatientAppointment(headers));
    }

    @GetMapping("/doctor/appointment")
    ResponseEntity<List<DoctorAppointmentResponse>> getDoctorAppointment(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointment(headers));
    }
}