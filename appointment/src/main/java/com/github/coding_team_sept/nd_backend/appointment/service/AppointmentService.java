package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.DoctorResponse;
import com.github.coding_team_sept.nd_backend.appointment.repository.AppointmentRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Stream;

@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo,
        RestTemplate restTemplate
) {
    public List<DoctorResponse> getAvailableDoctor(String timestamp) {
        final var datetime = DateTime.parse(timestamp, DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm"));
        System.out.println(datetime.toString());

        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                datetime.minus(15).toDate(),
                datetime.plus(15).toDate()
        ).stream().map(Appointment::getDoctorId).toList();

        final var result = restTemplate.getForObject(
                "http://www.localhost:9000/api/v1/doctor",
                DoctorResponse[].class
        );

        if (result != null) {
            return Stream.of(result).filter(
                    doctorResponse -> !occupiedDoctor.contains(doctorResponse.id())
            ).toList();
        }
        return List.of();
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