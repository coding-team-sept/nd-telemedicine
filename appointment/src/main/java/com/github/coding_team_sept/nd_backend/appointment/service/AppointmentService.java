package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.repository.AppointmentRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;

@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo
) {
    public String getAvailableDoctor(String timestamp) {
        final var datetime = DateTime.parse(timestamp, DateTimeFormat.forPattern("yyyy/MM/dd-HH:mm"));

        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                datetime.minus(15).toDate(),
                datetime.plus(15).toDate()
        ).stream().map(Appointment::getDoctorId).toList();

        // TODO: Retrieve doctor from Authentication Service excluding the occupiedDoctor.

        return "Available Doctor";
    }

    public String getAppointment() {
        // TODO: Implement get appointment
        return "Get appointment";
    }

    public String addAppointment() {
        // TODO: Implement add appointment
        return "Add appointment";
    }
}