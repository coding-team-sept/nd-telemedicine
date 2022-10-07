package com.github.coding_team_sept.nd_backend.appointment.services;

import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.*;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.format_exceptions.InvalidSessionException;
import com.github.coding_team_sept.nd_backend.appointment.models.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.models.AppointmentSession;
import com.github.coding_team_sept.nd_backend.appointment.payloads.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.AppointmentsResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.appointment.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.appointment.repositories.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.repositories.SessionRepository;
import com.github.coding_team_sept.nd_backend.appointment.utils.AppointmentDateTimeUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public record AppointmentService(
        AuthenticationService authService,
        ScheduleService scheduleService,
        AppointmentRepository appointmentRepo,
        SessionRepository sessionRepo,
        AppointmentDateTimeUtils datetimeUtils
) {
    private DateTime getDateTime(
            String datetime,
            Long patientId,
            Long doctorId
    ) throws AppointmentDateTimeException, AppointmentConflictException {
        final var appointmentDatetime = datetimeUtils.parseString(datetime);
        scheduleService.validateAppointmentDateTime(appointmentDatetime);
        if (patientId != null) {
            scheduleService.checkPatientAvailability(appointmentDatetime, patientId);
        }
        if (doctorId != null) {
            scheduleService.checkDoctorAvailability(appointmentDatetime, doctorId);
        }
        return appointmentDatetime;
    }

    private UserDataResponse getDoctor(
            HttpHeaders headers,
            Long doctorId
    ) throws RestClientException, UserNotFoundException {
        final var doctors = authService.getUsers(headers, List.of(doctorId), "doctor");
        if (!doctors.users.isEmpty()) {
            return doctors.users.get(0);
        }
        throw new UserNotFoundException();
    }

    public UsersDataResponse getAvailableDoctor(
            HttpHeaders headers,
            String datetime
    ) throws RestClientException,
            AppointmentDateTimeException,
            AppointmentConflictException {
        final var validation = authService.getAuthorization(headers);
        final var appointmentDatetime = getDateTime(datetime, validation.id, null);
        final var occupiedDoctor = appointmentRepo.getAppointmentByAppointmentTimeBetween(
                        datetimeUtils.getMin(appointmentDatetime).toDate(),
                        datetimeUtils.getMax(appointmentDatetime).toDate()
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
            AppointmentConflictException,
            InvalidSessionException {
        // Authorize requester and get its ID
        final var validation = authService.getAuthorization(headers);

        // Check session
        final var rawSession = body.session();
        AppointmentSession session;
        try {
            if (rawSession == null) {
                throw new IllegalArgumentException();
            } else {
                session = sessionRepo.findRoleByName(
                        SessionType.valueOf(body.session().toUpperCase())
                ).orElseThrow(IllegalArgumentException::new);
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidSessionException((rawSession == null) ? "NULL" : rawSession);
        }

        // Check doctor existence and get the doctor data
        final var doctorResponse = getDoctor(headers, body.doctorId());

        // Validate datetime and convert the type
        final var appointmentDatetime = getDateTime(body.datetime(), validation.id, doctorResponse.id);

        // Register appointment
        final var appointment = appointmentRepo.saveAndFlush(
                Appointment.builder()
                        .doctorId(doctorResponse.id)
                        .patientId(validation.id)
                        .appointmentTime(appointmentDatetime.toDate())
                        .session(session)
                        .build()
        );
        return PatientAppointmentResponse.build(
                appointment.getId(),
                doctorResponse,
                body.datetime(),
                appointment.getSession().getName()
        );
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
                    .filter(appointment -> datetimeUtils.getMax(
                                    new DateTime(appointment.getAppointmentTime())
                            ).isAfter(DateTime.now())
                    ).sorted(
                            Comparator.comparing(Appointment::getAppointmentTime)
                    ).map(appointment -> new PatientAppointmentResponse(
                            appointment.getId(),
                            doctors.users.stream()
                                    .filter(
                                            doctor -> doctor.id.equals(appointment.getDoctorId())
                                    ).findAny()
                                    .orElse(null),
                            appointment.getAppointmentTime().toString(),
                            appointment.getSession().getName()
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
                    .filter(appointment -> datetimeUtils.getMax(
                                    new DateTime(appointment.getAppointmentTime())
                            ).isAfter(DateTime.now())
                    ).sorted(
                            Comparator.comparing(Appointment::getAppointmentTime)
                    ).map(appointment -> new DoctorAppointmentResponse(
                            appointment.getId(),
                            patients.users.stream()
                                    .filter(
                                            patient -> patient.id.equals(appointment.getPatientId())
                                    ).findAny()
                                    .orElse(null),
                            appointment.getAppointmentTime().toString(),
                            appointment.getSession().getName()
                    )).filter(
                            appointment -> appointment.appointedUser != null
                    ).toList());
        }
        throw new UnauthorizedException("Unauthorized role");
    }
}