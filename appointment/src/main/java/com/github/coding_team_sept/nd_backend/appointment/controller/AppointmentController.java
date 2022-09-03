package com.github.coding_team_sept.nd_backend.appointment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public record AppointmentController() {
    // TODO: Create controller for handling appointment requests
    @GetMapping("/hello")
    String getHello() {
        return "Hello World";
    }
}