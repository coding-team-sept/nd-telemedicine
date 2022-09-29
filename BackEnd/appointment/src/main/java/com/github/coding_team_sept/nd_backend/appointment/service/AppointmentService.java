package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.exceptions.*;
import com.github.coding_team_sept.nd_backend.appointment.model.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.payload.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.appointment.AppointmentsResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.appointment.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.repository.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.utils.AppointmentDateTimeUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// TODO: Add check user's timetable apart from the doctor's timetable
@Service
public record AppointmentService(
        AppointmentRepository appointmentRepo,
        AuthenticationService authService,
        RestTemplate restTemplate,
        AppointmentDateTimeUtils datetimeUtils
) {
    public UsersDataResponse getAvailableDoctor(
            HttpHeaders headers,
            String datetime
    ) throws AppointmentDateTimeException, RestClientException {
        final var parsedDatetime = datetimeUtils.parseString(datetime);
        datetimeUtils.validateAppointmentDateTime(parsedDatetime);
        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                        datetimeUtils.getMin(parsedDatetime).toDate(),
                        datetimeUtils.getMax(parsedDatetime).toDate()
                ).stream()
                .map(Appointment::getDoctorId)
                .toList();

        final var doctors = authService.getUsers(headers, "doctor");
        if (!doctors.users.isEmpty()) {
            return UsersDataResponse.build(doctors.users.stream().filter(
                    doctorResponse -> !occupiedDoctor.contains(doctorResponse.id)
            ).toList());
        }
        return new UsersDataResponse(List.of());
    }

    public PatientAppointmentResponse addAppointment(
            HttpHeaders headers,
            AppointmentRequest body
    ) throws AppointmentDateTimeException,
            RestClientException,
            UserNotFoundException,
            AppointmentConflictException {
        // Authorize requester and get its ID
        final var validation = authService.getAuthorization(headers);

        // Check doctor existence and get the doctor data
        final var doctorResponse = getDoctor(headers, body.doctorId());

        // Validate datetime and convert the type
        final var appointmentDatetime = getDateTime(body.datetime(), body.doctorId());

        // Register appointment
        final var appointment = appointmentRepo.saveAndFlush(
                Appointment.builder()
                        .doctorId(doctorResponse.id)
                        .patientId(validation.id)
                        .appointmentTime(appointmentDatetime.toDate()).build()
        );
        return PatientAppointmentResponse.build(
                appointment.getId(),
                doctorResponse,
                body.datetime()
        );
    }

    private DateTime getDateTime(
            String datetime,
            Long doctorId
    ) throws AppointmentDateTimeException, AppointmentConflictException {
        final var appointmentDatetime = datetimeUtils.parseString(datetime);
        if (datetimeUtils.validateAppointmentDateTime(appointmentDatetime)) {
            if (!appointmentRepo.existsAppointmentByDoctorIdAndAppointmentTimeBetween(
                    doctorId,
                    datetimeUtils.getMin(appointmentDatetime).toDate(),
                    datetimeUtils.getMax(appointmentDatetime).toDate()
            )) {
                return appointmentDatetime;
            }
        }
        throw new AppointmentConflictException("Doctor occupied");
    }

    private UserDataResponse getDoctor(
            HttpHeaders headers,
            Long doctorId
    ) throws UserNotFoundException {
        final var doctors = authService.getUsers(headers, List.of(doctorId), "doctor");
        if (!doctors.users.isEmpty()) {
            return doctors.users.get(0);
        }
        throw new UserNotFoundException();
    }

    public AppointmentsResponse<PatientAppointmentResponse> getPatientAppointment(
            HttpHeaders headers
    ) throws RestClientException, UnauthorizedException {
        final var validation = authService.getAuthorization(headers);
        final var role = validation.role.toLowerCase();
        if (role.contains("patient")) {
            final var appointments = appointmentRepo.getAppointmentByPatientId(validation.id);
            final var doctorsId = appointments.stream()
                    .map(Appointment::getDoctorId)
                    .distinct()
                    .toList();

            // Retrieve doctors data
            final var doctors = authService.getUsers(headers, doctorsId, "doctor");
            return AppointmentsResponse.build(appointments.stream()
                    .map(appointment -> new PatientAppointmentResponse(
                            appointment.getId(),
                            doctors.users.stream()
                                    .filter(
                                            doctor -> doctor.id.equals(appointment.getDoctorId())
                                    ).findAny()
                                    .orElse(null),
                            appointment.getAppointmentTime().toString()
                    )).filter(
                            appointment -> appointment.appointedUser != null
                    ).toList());
        }
        throw new UnauthorizedException("Unauthorized role");
    }

    public AppointmentsResponse<DoctorAppointmentResponse> getDoctorAppointment(
            HttpHeaders headers
    ) throws RestClientException, UnauthorizedException {
        final var validation = authService.getAuthorization(headers);
        final var role = validation.role.toLowerCase();
        if (role.contains("doctor")) {
            final var appointments = appointmentRepo.getAppointmentByDoctorId(validation.id);
            final var patientsId = appointments.stream()
                    .map(Appointment::getPatientId)
                    .distinct()
                    .toList();

            // Retrieve doctors data
            final var patients = authService.getUsers(headers, patientsId, "patient");
            return AppointmentsResponse.build(appointments.stream()
                    .map(appointment -> new DoctorAppointmentResponse(
                            appointment.getId(),
                            patients.users.stream()
                                    .filter(
                                            patient -> patient.id.equals(appointment.getPatientId())
                                    ).findAny()
                                    .orElse(null),
                            appointment.getAppointmentTime().toString()
                    )).filter(
                            appointment -> appointment.appointedUser != null
                    ).toList());
        }
        throw new UnauthorizedException("Unauthorized role");
    }
}