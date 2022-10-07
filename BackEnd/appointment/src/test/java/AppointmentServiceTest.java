import com.github.coding_team_sept.nd_backend.appointment.enums.SessionType;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentConflictException;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.RestClientException;
import com.github.coding_team_sept.nd_backend.appointment.models.Appointment;
import com.github.coding_team_sept.nd_backend.appointment.models.AppointmentSession;
import com.github.coding_team_sept.nd_backend.appointment.payloads.requests.AppointmentRequest;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ValidateResponse;
import com.github.coding_team_sept.nd_backend.appointment.repositories.AppointmentRepository;
import com.github.coding_team_sept.nd_backend.appointment.repositories.SessionRepository;
import com.github.coding_team_sept.nd_backend.appointment.services.AppointmentService;
import com.github.coding_team_sept.nd_backend.appointment.services.AuthenticationService;
import com.github.coding_team_sept.nd_backend.appointment.services.ScheduleService;
import com.github.coding_team_sept.nd_backend.appointment.utils.AppointmentDateTimeUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTest {
    static final DateTime appointmentDatetime = DateTime.now()
            .plusDays(1)
            .withTime(9, 0, 0, 0);
    static final List<Long> sampleDoctorIds = List.of(100L, 101L, 102L);

    HttpHeaders headers = new HttpHeaders();
    @Mock
    AppointmentDateTimeUtils dateTimeUtils;
    @Mock
    AuthenticationService authenticationService;
    @Mock
    ScheduleService scheduleService;
    @Mock
    SessionRepository sessionRepository;
    @Mock
    AppointmentRepository appointmentRepo;
    @InjectMocks
    AppointmentService appointmentService;

    // [AppointmentDateTimeUtils] mock
    void mockParseDateTime(String datetime) {
        Mockito.when(dateTimeUtils.parseString(datetime)).thenCallRealMethod();
    }

    void mockGetMin(DateTime appointmentDatetime) {
        Mockito.when(dateTimeUtils.getMin(appointmentDatetime)).thenCallRealMethod();
    }

    void mockGetMax(DateTime appointmentDatetime) {
        Mockito.when(dateTimeUtils.getMax(appointmentDatetime)).thenCallRealMethod();
    }

    // [AuthenticationService] mock
    void mockAuthorization(
            HttpHeaders headers,
            ValidateResponse validate,
            boolean isSuccessful
    ) {
        if (isSuccessful) {
            Mockito.when(
                    authenticationService.getAuthorization(headers)
            ).thenReturn(validate);
        } else {
            Mockito.when(
                    authenticationService.getAuthorization(headers)
            ).thenThrow(
                    RestClientException.build(HttpStatus.UNAUTHORIZED, "403")
            );
        }
    }

    void mockGetUsers(
            HttpHeaders headers,
            String target,
            List<UserDataResponse> users,
            boolean useIds,
            boolean isSuccessful
    ) {
        Mockito.when(
                (useIds)
                        ? authenticationService.getUsers(
                        headers,
                        users.stream()
                                .map(
                                        (user) -> user.id
                                ).toList(),
                        target
                )
                        : authenticationService.getUsers(headers, target)
        ).thenReturn(UsersDataResponse.build(
                (isSuccessful)
                        ? users
                        : List.of()
        ));
    }

    // [ScheduleService] mock
    void mockValidateAppointmentDateTime(
            DateTime appointmentDatetime
    ) {
        Mockito.doNothing()
                .doCallRealMethod()
                .when(
                        scheduleService
                ).validateAppointmentDateTime(appointmentDatetime);
    }

    void mockCheckAvailability(
            DateTime appointmentDatetime,
            String target,
            Long targetId,
            boolean isAvailable
    ) {
        if (isAvailable) {
            if (target.equalsIgnoreCase("doctor")) {
                Mockito.doNothing()
                        .when(scheduleService)
                        .checkDoctorAvailability(
                                appointmentDatetime,
                                targetId
                        );
            } else {
                Mockito.doNothing()
                        .when(scheduleService)
                        .checkPatientAvailability(
                                appointmentDatetime,
                                targetId
                        );
            }
        } else {
            Mockito.doThrow(AppointmentConflictException.class)
                    .when(scheduleService)
                    .checkPatientAvailability(
                            appointmentDatetime,
                            targetId
                    );
        }
    }

    // [SessionRepository] mock
    AppointmentSession getSession(SessionType sessionType) {
        return AppointmentSession.builder()
                .id(Integer.valueOf(sessionType.ordinal()).longValue())
                .name(sessionType)
                .build();
    }

    void mockSessionRepository(SessionType sessionType) {
        Mockito.when(
                sessionRepository.findRoleByName(sessionType)
        ).thenReturn(
                Optional.of(getSession(sessionType))
        );
    }

    // [AppointmentRepository] mock
    void mockGetAppointmentByDateTimeBetween(
            DateTime appointmentDatetime,
            List<Appointment> appointments
    ) {
        Mockito.when(
                appointmentRepo.getAppointmentByAppointmentTimeBetween(
                        dateTimeUtils.getMin(appointmentDatetime).toDate(),
                        dateTimeUtils.getMax(appointmentDatetime).toDate()
                )
        ).thenReturn(appointments);
    }

    void mockGetAppointmentById(
            Long id,
            String requester,
            List<Appointment> appointments
    ) {
        Mockito.when(
                (requester.equalsIgnoreCase("doctor"))
                        ? appointmentRepo.getAppointmentByDoctorId(id)
                        : appointmentRepo.getAppointmentByPatientId(id)
        ).thenReturn(appointments);
    }

    void mockSaveAndFlush(Appointment appointment) {
        Mockito.when(
                appointmentRepo.saveAndFlush(
                        Appointment.builder()
                                .doctorId(appointment.getDoctorId())
                                .patientId(appointment.getPatientId())
                                .appointmentTime(appointment.getAppointmentTime())
                                .session(appointment.getSession())
                                .build()
                )
        ).thenReturn(appointment);
    }

    void mockGetDateTime(DateTime appointmentDatetime) {
        mockParseDateTime(appointmentDatetime.toString(
                AppointmentDateTimeUtils.pattern
        ));
        mockGetMin(appointmentDatetime);
        mockGetMax(appointmentDatetime);
        mockValidateAppointmentDateTime(appointmentDatetime);
    }

    /** Test a successful [getAvailableDoctor] method
     * Scenario: A patient with id(0) looks for doctors available at a specific timeframe.
     * There are 3 doctors (ids: 100, 101, 102) in the database. The doctor with id(100)
     * already has an appointment with patient id(1) at the same time specified by patient id(0).
     * The expected result is an array which contains only 2 doctors (ids: 101, 102).
     * */
    @Test
    void testGetAvailableDoctor() {
        // Setup data
        final var stringDatetime = appointmentDatetime.toString(AppointmentDateTimeUtils.pattern);
        final var patientValidation = new ValidateResponse(0L, "PATIENT_ROLE");
        final var doctors = sampleDoctorIds.stream()
                .map(sampleDoctorId -> UserDataResponse.build(
                        sampleDoctorId,
                        "doctor" + sampleDoctorId + "@doctor.com",
                        "Doctor"
                )).toList();
        final var appointments = List.of(
                Appointment.builder()
                        .id(doctors.get(0).id)
                        .patientId(1L)
                        .doctorId(doctors.get(0).id)
                        .session(getSession(SessionType.OFFLINE))
                        .appointmentTime(appointmentDatetime.toDate())
                        .build()
        );

        // Create mocks
        mockAuthorization(headers, patientValidation, true);
        mockGetDateTime(appointmentDatetime);
        mockCheckAvailability(
                appointmentDatetime,
                "patient",
                patientValidation.id,
                true
        );
        mockGetAppointmentByDateTimeBetween(appointmentDatetime, appointments);
        mockGetUsers(
                headers,
                "doctor",
                doctors,
                false,
                true
        );

        // Test
        final var response = appointmentService.getAvailableDoctor(headers, stringDatetime);
        Assertions.assertEquals(2, response.users.size());
        final var responseIds = response.users.stream().map(userDataResponse -> userDataResponse.id).toList();
        Assertions.assertTrue(responseIds.contains(doctors.get(1).id));
        Assertions.assertTrue(responseIds.contains(doctors.get(2).id));
    }

    @Test
    void testAddAppointment() {
        final var patientValidation = new ValidateResponse(0L, "PATIENT_ROLE");
        final var doctors = sampleDoctorIds.stream()
                .map(sampleDoctorId -> UserDataResponse.build(
                        sampleDoctorId,
                        "doctor" + sampleDoctorId + "@doctor.com",
                        "Doctor"
                ))
                .toList();
        final var stringDatetime = appointmentDatetime.toString(
                AppointmentDateTimeUtils.pattern
        );
        final var appointment = Appointment.builder()
                .id(0L)
                .patientId(patientValidation.id)
                .doctorId(doctors.get(0).id)
                .session(getSession(SessionType.OFFLINE))
                .appointmentTime(appointmentDatetime.toDate())
                .build();
        mockAuthorization(headers, patientValidation, true);
        mockSessionRepository(SessionType.OFFLINE);
        mockGetUsers(
                headers,
                "doctor",
                List.of(doctors.get(0)),
                true,
                true
        );
        mockGetDateTime(appointmentDatetime);
        mockCheckAvailability(
                appointmentDatetime,
                "patient",
                patientValidation.id,
                true
        );
        mockCheckAvailability(
                appointmentDatetime,
                "doctor",
                doctors.get(0).id,
                true
        );
        mockSaveAndFlush(appointment);
        appointmentService.addAppointment(
                headers,
                new AppointmentRequest(
                        doctors.get(0).id,
                        stringDatetime,
                        SessionType.OFFLINE.name()
                )
        );
    }

    @Test
    void testGetPatientAppointment() {
        final var patientValidation = new ValidateResponse(0L, "PATIENT_ROLE");
        final var doctors = sampleDoctorIds.stream()
                .map(sampleDoctorId -> UserDataResponse.build(
                        sampleDoctorId,
                        "doctor" + sampleDoctorId + "@doctor.com",
                        "Doctor"
                )).toList();
        final var appointments = doctors.stream()
                .map(doctor -> Appointment.builder()
                        .id(doctor.id)
                        .patientId(patientValidation.id)
                        .doctorId(doctor.id)
                        .appointmentTime(appointmentDatetime.toDate())
                        .session(getSession(SessionType.OFFLINE))
                        .build()
                ).toList();
        mockAuthorization(headers, patientValidation, true);
        mockGetAppointmentById(
                patientValidation.id,
                "patient",
                appointments
        );
        mockGetUsers(
                headers,
                "doctor",
                doctors,
                true,
                true
        );
        mockGetMax(appointmentDatetime);
        appointmentService.getPatientAppointment(headers);
    }

    @Test
    void testGetDoctorAppointment() {
        final var doctorValidation = new ValidateResponse(100L, "DOCTOR_ROLE");
        final var patients = List.of(
                UserDataResponse.build(
                        0L,
                        "patient0@patient.com",
                        "Patient"
                )
        );
        final var appointments = patients.stream()
                .map(patient -> Appointment.builder()
                        .id(patient.id)
                        .patientId(patient.id)
                        .doctorId(doctorValidation.id)
                        .appointmentTime(appointmentDatetime.toDate())
                        .session(getSession(SessionType.OFFLINE))
                        .build()
                ).toList();
        mockAuthorization(headers, doctorValidation, true);
        mockGetAppointmentById(
                doctorValidation.id,
                "doctor",
                appointments
        );
        mockGetUsers(
                headers,
                "patient",
                patients,
                true,
                true
        );
        mockGetMax(appointmentDatetime);
        appointmentService.getDoctorAppointment(headers);
    }
}
