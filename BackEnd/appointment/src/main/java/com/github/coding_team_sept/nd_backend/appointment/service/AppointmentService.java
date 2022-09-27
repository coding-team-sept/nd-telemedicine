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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

// TODO: Handle failed HTTP requests (e.g. 400, 401)
@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo,
        RestTemplate restTemplate,
        DateTimeUtils dateTimeUtils
) {
    private static final String url = "http://localhost:9000/api/v1";

    public UsersDataResponse getAvailableDoctor(HttpHeaders headers, String datetime) throws IOException {
        final var parsedDatetime = dateTimeUtils.parseString(datetime);

        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                dateTimeUtils.getMin(parsedDatetime).toDate(),
                dateTimeUtils.getMax(parsedDatetime).toDate()
        ).stream().map(Appointment::getDoctorId).toList();

        final var result = restTemplate.exchange(
                url + "/app/admin/doctor",
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
                        .doctorId(doctorResponse.id)
                        .patientId(patientId)
                        .appointmentTime(appointmentDatetime).build()
        );

        return PatientAppointmentResponse.build(
                appointment.getId(),
                doctorResponse,
                body.datetime()
        );
    }

    public List<PatientAppointmentResponse> getPatientAppointment(HttpHeaders headers) {
//        final var httpValidateResponse = restTemplate.exchange(
//                url + "/auth/validate",
//                HttpMethod.GET,
//                new HttpEntity<>(headers),
//                ValidateResponse.class
//        );
//        if (httpValidateResponse.getBody() != null) {
//            final var validateResponse = httpValidateResponse.getBody();
//            final var role = validateResponse.role().toLowerCase();
//            if (role.contains("patient")) {
//                final var appointments = appointmentRepo.getAppointmentByPatientId(validateResponse.id());
//                final var doctorsId = appointments.stream()
//                        .map(Appointment::getDoctorId)
//                        .distinct()
//                        .toList();
//
//                // Retrieve doctors data
//                final var doctors = getUsers(headers, doctorsId, "doctor");
//                return appointments.stream()
//                        .map(appointment -> new PatientAppointmentResponse(
//                                appointment.getId(),
//                                doctors.stream()
//                                        .filter(
//                                                doctor -> doctor.id().equals(appointment.getDoctorId())
//                                        ).findAny()
//                                        .orElse(null),
//                                appointment.getAppointmentTime().toString()
//                        )).filter(
//                                appointment -> appointment.doctor != null
//                        ).toList();
//            }
//        }
        return null;
    }

    public List<DoctorAppointmentResponse> getDoctorAppointment(HttpHeaders headers) {
        final var httpValidateResponse = restTemplate.exchange(
                url + "/auth/validate",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ValidateResponse.class
        );
        if (httpValidateResponse.getBody() != null) {
            final var validateResponse = httpValidateResponse.getBody();
            final var role = validateResponse.role().toLowerCase();
            if (role.contains("doctor")) {
                final var appointments = appointmentRepo.getAppointmentByDoctorId(validateResponse.id());
                final var patientsId = appointments.stream()
                        .map(Appointment::getPatientId)
                        .distinct()
                        .toList();

                // Retrieve doctors data
                final var patients = getUsers(headers, patientsId, "patient");
                return appointments.stream()
                        .map(appointment -> new DoctorAppointmentResponse(
                                appointment.getId(),
                                patients.stream()
                                        .filter(
                                                patient -> patient.id().equals(appointment.getPatientId())
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

    private List<AppUserResponse> getUsers(HttpHeaders headers, List<Long> ids, String subject) {
        String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/" + subject)
                .queryParam("ids", ids)
                .encode()
                .toUriString();

        final var httpUserResponse = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                AppUserResponse[].class
        );

        if (httpUserResponse.getBody() != null) {
            return List.of(httpUserResponse.getBody());
        }
        return List.of();
    }

    private Long authorizeAndGetId(HttpHeaders headers) {
        // Check "Authorization"
        final var httpValidateResponse = restTemplate.exchange(
                url + "/auth/validate",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ValidateResponse.class
        );

        if (httpValidateResponse.getBody() != null) {
            return httpValidateResponse.getBody().id();
        }
        return null;
    }

    private UserDataResponse validateAndGetDoctor(HttpHeaders headers, Long doctorId) {
        String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/admin/doctor")
                .queryParam("ids", List.of(doctorId))
                .encode()
                .toUriString();

        final var httpDoctorResponse = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        );

        if (httpDoctorResponse.getBody() != null && httpDoctorResponse.getBody().data != null) {
            final var doctorResponses = httpDoctorResponse.getBody().data;

            if (doctorResponses.users != null && doctorResponses.users.size() > 0) {
                return doctorResponses.users.get(0);
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