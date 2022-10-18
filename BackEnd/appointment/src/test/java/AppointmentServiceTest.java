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
import com.github.coding_team_sept.nd_backend.appointment.services.ChatService;
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
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTest {
    private static final DateTime appointmentDatetime = DateTime.now()
            .plusDays(1)
            .withTime(9, 0, 0, 0);
    private static final List<Long> sampleDoctorIds = List.of(100L, 101L, 102L);

    private final HttpHeaders headers = new HttpHeaders();
    @Mock
    private AppointmentDateTimeUtils dateTimeUtils;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ChatService chatService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private AppointmentRepository appointmentRepo;
    @InjectMocks
    private AppointmentService appointmentService;

    // [AppointmentDateTimeUtils] mock
    private void mockParseDateTime(String datetime) {
        Mockito.when(dateTimeUtils.parseString(datetime)).thenCallRealMethod();
    }

    private void mockGetMin(DateTime appointmentDatetime) {
        Mockito.when(dateTimeUtils.getMin(appointmentDatetime)).thenCallRealMethod();
    }

    private void mockGetMax(DateTime appointmentDatetime) {
        Mockito.when(dateTimeUtils.getMax(appointmentDatetime)).thenCallRealMethod();
    }

    // [AuthenticationService] mock
    private void mockAuthorization(
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

    private void mockGetUsers(
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
    private void mockValidateAppointmentDateTime(
            DateTime appointmentDatetime
    ) {
        Mockito.doNothing()
                .doCallRealMethod()
                .when(
                        scheduleService
                ).validateAppointmentDateTime(appointmentDatetime);
    }

    private void mockCheckAvailability(
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
    private AppointmentSession getSession(SessionType sessionType) {
        return AppointmentSession.builder()
                .id(Integer.valueOf(sessionType.ordinal()).longValue())
                .name(sessionType)
                .build();
    }

    private void mockSessionRepository(SessionType sessionType) {
        Mockito.when(
                sessionRepository.findRoleByName(sessionType)
        ).thenReturn(
                Optional.of(getSession(sessionType))
        );
    }

    // [AppointmentRepository] mock
    private void mockGetAppointmentByDateTimeBetween(
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

    private void mockGetAppointmentById(
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

    private void mockSaveAndFlush(Appointment appointment) {
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

    private void mockGetDateTime(DateTime appointmentDatetime) {
        mockParseDateTime(appointmentDatetime.toString(
                AppointmentDateTimeUtils.pattern
        ));
        mockGetMin(appointmentDatetime);
        mockGetMax(appointmentDatetime);
        mockValidateAppointmentDateTime(appointmentDatetime);
    }

    /**
     * Test a successful [getAvailableDoctor] method.
     * Scenario: A patient with id(0) looks for doctors available at a specific timeframe.
     * There are 3 doctors (ids: 100, 101, 102) in the database. The doctor with id(100)
     * already has an appointment with patient id(1) at the same time specified by patient id(0).
     * The expected result is an array which contains only 2 doctors (ids: 101, 102).
     */
    @Test
    public void testGetAvailableDoctor() {
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

    /**
     * Test a successful [addAppointment] method.
     * Scenario: A patient with id(0) makes an appointment with doctor with id(100).
     * Both patient and doctor are available at the specified time, therefore the
     * appointment is successfully made.
     */
    @Test
    public void testAddAppointment() {
        // Setup data
        final var patientValidation = new ValidateResponse(0L, "PATIENT_ROLE");
        final var doctor = UserDataResponse.build(
                sampleDoctorIds.get(0),
                "doctor" + sampleDoctorIds.get(0) + "@doctor.com",
                "Doctor"
        );
        final var stringDatetime = appointmentDatetime.toString(AppointmentDateTimeUtils.pattern);
        final var appointment = Appointment.builder()
                .id(doctor.id)
                .patientId(patientValidation.id)
                .doctorId(doctor.id)
                .appointmentTime(appointmentDatetime.toDate())
                .session(getSession(SessionType.OFFLINE))
                .build();

        // Create mocks
        mockAuthorization(headers, patientValidation, true);
        mockSessionRepository(SessionType.OFFLINE);
        mockGetUsers(
                headers,
                "doctor",
                List.of(doctor),
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
                doctor.id,
                true
        );
        mockSaveAndFlush(appointment);

        // Test
        final var response = appointmentService.addAppointment(
                headers,
                new AppointmentRequest(
                        doctor.id,
                        stringDatetime,
                        SessionType.OFFLINE.name()
                )
        );
        Assertions.assertEquals(doctor.id, response.appointedUser.id);
        Assertions.assertEquals(stringDatetime, response.datetime);
    }

    /**
     * Test a successful [getPatientAppointment] method.
     * Scenario: A patient with id(0) gets all of its appointment. The patient has 3 appointments
     * with doctor (ids: 100, 101, 102). Only the appointment with doctor of id(100) was done yesterday.
     * The rest of the appointments will be done in the upcoming days. Thus, the appointment with
     * doctor id(0) will not appear in the appointments list.
     */
    @Test
    public void testGetPatientAppointment() {
        // Setup data
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
                        .appointmentTime(
                                (doctor.id.equals(doctors.get(0).id))
                                        ? appointmentDatetime.minusDays(2).toDate()
                                        : appointmentDatetime.plusDays(doctor.id.intValue() % 100).toDate()
                        ).session(getSession(SessionType.OFFLINE))
                        .build()
                ).toList();

        // Create mocks
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
        appointments.forEach(appointment -> mockGetMax(new DateTime(appointment.getAppointmentTime())));

        // Test
        final var response = appointmentService.getPatientAppointment(headers, null);
        Assertions.assertEquals(2, response.appointments.size());
        Assertions.assertTrue(response.appointments.stream().noneMatch(appointment -> appointment.id.equals(doctors.get(0).id)));
    }

    /**
     * Test a successful [getDoctorAppointment] method.
     * Scenario: A doctor with id(0) gets all of its appointment. The doctor has 3 appointments
     * with patients (ids: 0, 1, 2). Only the appointment with patient of id(0) was done yesterday.
     * The rest of the appointments will be done in the upcoming days. Thus, the appointment with
     * patient id(0) will not appear in the appointments list.
     */
    @Test
    public void testGetDoctorAppointment() {
        // Setup data
        final var doctorValidation = new ValidateResponse(100L, "DOCTOR_ROLE");
        final var patients = Stream.of(0, 1, 2)
                .map(id -> UserDataResponse.build(
                        id.longValue(),
                        "patient" + id + "@patient.com",
                        "Patient"
                )).toList();
        final var appointments = patients.stream()
                .map(patient -> Appointment.builder()
                        .id(patient.id)
                        .patientId(patient.id)
                        .doctorId(doctorValidation.id)
                        .appointmentTime(
                                (patient.id.equals(patients.get(0).id))
                                        ? appointmentDatetime.minusDays(2).toDate()
                                        : appointmentDatetime.plusDays(patient.id.intValue()).toDate()
                        ).session(getSession(SessionType.OFFLINE))
                        .build()
                ).toList();

        // Create mocks
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
        appointments.forEach(appointment -> mockGetMax(new DateTime(appointment.getAppointmentTime())));

        // Test
        final var response = appointmentService.getDoctorAppointment(headers, null);
        Assertions.assertEquals(2, response.appointments.size());
        Assertions.assertTrue(response.appointments.stream().noneMatch(appointment -> appointment.id.equals(patients.get(0).id)));
    }
}
