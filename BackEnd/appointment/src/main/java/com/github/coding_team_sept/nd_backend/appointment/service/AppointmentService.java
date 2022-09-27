package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.payload.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.*;
import com.github.coding_team_sept.nd_backend.appointment.repository.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.utils.DateTimeUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

// TODO: Handle failed HTTP requests (e.g. 400, 401)
@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo,
        AuthenticationService authService,
        RestTemplate restTemplate,
        DateTimeUtils dateTimeUtils
) {
    public UsersDataResponse getAvailableDoctor(HttpHeaders headers, String datetime) {
        final var parsedDatetime = dateTimeUtils.parseString(datetime);

        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                        dateTimeUtils.getMin(parsedDatetime).toDate(),
                        dateTimeUtils.getMax(parsedDatetime).toDate()
                ).stream()
                .map(Appointment::getDoctorId)
                .toList();

        final var result = restTemplate.exchange(
                AuthenticationService.url + "/app/admin/doctor",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        );

        if (result.getBody() != null) {
            final var doctorResponses = result.getBody().data;
            return UsersDataResponse.build(doctorResponses.users.stream().filter(
                    doctorResponse -> !occupiedDoctor.contains(doctorResponse.id)
            ).toList());
        }

        return new UsersDataResponse(List.of());
    }

    public PatientAppointmentResponse addAppointment(
            HttpHeaders headers,
            AppointmentRequest body
    ) {
        // Authorize requester and get its ID
        final var validation = authService.getAuthorization(headers);
        if (validation == null) {
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
                        .doctorId(doctorResponse.id)
                        .patientId(validation.id)
                        .appointmentTime(appointmentDatetime).build()
        );

        return PatientAppointmentResponse.build(
                appointment.getId(),
                doctorResponse,
                body.datetime()
        );
    }

    public List<PatientAppointmentResponse> getPatientAppointment(HttpHeaders headers) {
        final var validation = authService.getAuthorization(headers);
        if (validation != null) {
            final var role = validation.role.toLowerCase();
            if (role.contains("patient")) {
                final var appointments = appointmentRepo.getAppointmentByPatientId(validation.id);
                final var doctorsId = appointments.stream()
                        .map(Appointment::getDoctorId)
                        .distinct()
                        .toList();

                // Retrieve doctors data
                final var doctors = authService.getUsers(headers, doctorsId, "doctor");
                return appointments.stream()
                        .map(appointment -> new PatientAppointmentResponse(
                                appointment.getId(),
                                doctors.users.stream()
                                        .filter(
                                                doctor -> doctor.id.equals(appointment.getDoctorId())
                                        ).findAny()
                                        .orElse(null),
                                appointment.getAppointmentTime().toString()
                        )).filter(
                                appointment -> appointment.doctor != null
                        ).toList();
            }
        }
        return null;
    }

    public List<DoctorAppointmentResponse> getDoctorAppointment(HttpHeaders headers) {
        final var validation = authService.getAuthorization(headers);
        if (validation != null) {
            final var role = validation.role.toLowerCase();
            if (role.contains("doctor")) {
                final var appointments = appointmentRepo.getAppointmentByDoctorId(validation.id);
                final var patientsId = appointments.stream()
                        .map(Appointment::getPatientId)
                        .distinct()
                        .toList();

                // Retrieve doctors data
                final var patients = authService.getUsers(headers, patientsId, "patient");
                return appointments.stream()
                        .map(appointment -> new DoctorAppointmentResponse(
                                appointment.getId(),
                                patients.users.stream()
                                        .filter(
                                                patient -> patient.id.equals(appointment.getPatientId())
                                        ).findAny()
                                        .orElse(null),
                                appointment.getAppointmentTime().toString()
                        )).filter(
                                appointment -> appointment.patient() != null
                        ).toList();
            }
        }
        return null;
    }

    private UserDataResponse validateAndGetDoctor(HttpHeaders headers, Long doctorId) {
        final var doctors = authService.getUsers(headers, List.of(doctorId), "doctor");

        if (!doctors.users.isEmpty()) {
            return doctors.users.get(0);
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