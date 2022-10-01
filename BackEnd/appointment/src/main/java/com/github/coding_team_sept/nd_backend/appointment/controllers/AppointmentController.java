package com.github.coding_team_sept.nd_backend.appointment.controllers;

import com.github.coding_team_sept.nd_backend.appointment.payloads.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.AppointmentsResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.services.AppointmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<ResponseWrapper<PatientAppointmentResponse>> addAppointment(
            @RequestHeader HttpHeaders headers,
            @RequestBody AppointmentRequest body
    ) {
        return ResponseEntity.ok(ResponseWrapper.fromData(appointmentService.addAppointment(headers, body)));
    }

    @GetMapping("/patient/appointment")
    ResponseEntity<ResponseWrapper<AppointmentsResponse<PatientAppointmentResponse>>> getPatientAppointment(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(ResponseWrapper.fromData(appointmentService.getPatientAppointment(headers)));
    }

    @GetMapping("/doctor/appointment")
    ResponseEntity<ResponseWrapper<AppointmentsResponse<DoctorAppointmentResponse>>> getDoctorAppointment(@RequestHeader HttpHeaders headers) {
        return ResponseEntity.ok(ResponseWrapper.fromData(appointmentService.getDoctorAppointment(headers)));
    }
}