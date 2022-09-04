package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.payload.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.AppUserResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.AppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.repository.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.utils.DateTimeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

// TODO: Handle failed HTTP requests (e.g. 400, 401)
@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo,
        RestTemplate restTemplate,
        DateTimeUtils dateTimeUtils
) {
    public List<AppUserResponse> getAvailableDoctor(HttpHeaders headers, String datetime) {
        final var parsedDatetime = dateTimeUtils.parseString(datetime);

        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                dateTimeUtils.getMin(parsedDatetime).toDate(),
                dateTimeUtils.getMax(parsedDatetime).toDate()
        ).stream().map(Appointment::getDoctorId).toList();


        final var result = restTemplate.exchange(
                "http://www.localhost:9000/api/v1/doctor",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AppUserResponse[].class
        );

        if (result.getBody() != null) {
            final var doctorResponses = result.getBody();
            return Stream.of(doctorResponses).filter(
                    doctorResponse -> !occupiedDoctor.contains(doctorResponse.id())
            ).toList();
        }
        return List.of();
    }

    public String getAppointment() {
        // TODO: Implement get appointment
        return "Get appointment";
    }

    public AppointmentResponse addAppointment(HttpHeaders headers, AppointmentRequest body) {
        // Authorize requester and get its ID
        final var patientId = authorizeAndGetId(headers);
        if (patientId == null) {
            // TODO: Throw patient not found error
            return null;
        }

        // Check doctor existence and get the doctor data
        final var doctorResponse = validateAndGetDoctor(headers, body.doctorId());
        if (doctorResponse == null) {
            // TODO: Throw patient not found error
            return null;
        }

        // Check if datetime valid
        final var appointmentDatetime = validateAppointmentDatetime(body.datetime(), body.doctorId());
        if (appointmentDatetime == null) {
            // TODO: Throw patient not found error
            return null;
        }

        // Register appointment
        final var appointment = appointmentRepo.saveAndFlush(
                Appointment.builder()
                        .doctorId(doctorResponse.id())
                        .patientId(patientId)
                        .appointmentTime(appointmentDatetime).build()
        );

        return new AppointmentResponse(
                appointment.getId(),
                doctorResponse,
                body.datetime()
        );
    }

    private Long authorizeAndGetId(HttpHeaders headers) {
        // Check "Authorization"
        final var httpValidateResponse = restTemplate.exchange(
                "http://localhost:9000/api/v1/validate",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Long.class
        );

        if (httpValidateResponse.hasBody()) {
            return httpValidateResponse.getBody();
        }
        return null;
    }

    private AppUserResponse validateAndGetDoctor(HttpHeaders headers, Long doctorId) {
        String uri = UriComponentsBuilder.fromHttpUrl("http://www.localhost:9000/api/v1/doctor")
                .queryParam("id", doctorId)
                .encode()
                .toUriString();

        final var httpDoctorResponse = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AppUserResponse[].class
        );

        if (httpDoctorResponse.hasBody()) {
            final var doctorResponses = httpDoctorResponse.getBody();
            if (doctorResponses != null && doctorResponses.length > 0) {
                return doctorResponses[0];
            }
        }
        return null;
    }

    private Date validateAppointmentDatetime(String datetime, Long doctorId) {
        final var appointmentDatetime = dateTimeUtils.parseString(datetime);

        // TODO: Validate datetime

        if (!appointmentRepo.existsAppointmentByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                dateTimeUtils.getMin(appointmentDatetime).toDate(),
                dateTimeUtils.getMax(appointmentDatetime).toDate()
        )) {
            return appointmentDatetime.toDate();
        }
        return null;
    }
}