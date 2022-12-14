import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentConflictException;
import com.github.coding_team_sept.nd_backend.appointment.exceptions.AppointmentDateTimeException;
import com.github.coding_team_sept.nd_backend.appointment.repositories.AppointmentRepository;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ScheduleServiceTest {
    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private AppointmentDateTimeUtils datetimeUtils;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    public void testValidateAppointmentDateTimeWithValidDateTime() {
        final var datetime = DateTime.now()
                .plusDays(1)
                .withTime(ScheduleService.openTime.plusHours(1));
        Assertions.assertDoesNotThrow(
                () -> scheduleService.validateAppointmentDateTime(datetime)
        );
    }

    @Test
    public void testValidateAppointmentDateTimeWithInvalidDateTime() {
        // Past datetime
        final var datetime1 = DateTime.now()
                .minusDays(1)
                .withTime(ScheduleService.openTime.plusHours(1));
        var thrown = Assertions.assertThrows(
                AppointmentDateTimeException.class,
                () -> scheduleService.validateAppointmentDateTime(datetime1)
        );
        Assertions.assertTrue(thrown.message.contains("Datetime in the past"));

        // Above 1 year time limit
        final var datetime2 = DateTime.now()
                .plusYears(2);
        thrown = Assertions.assertThrows(
                AppointmentDateTimeException.class,
                () -> scheduleService.validateAppointmentDateTime(datetime2)
        );
        Assertions.assertTrue(thrown.message.contains("Passed the limit of appointment (1 year)"));

        // Before open hour
        final var datetime3 = DateTime.now()
                .plusDays(1)
                .withTime(ScheduleService.openTime.minusHours(1));
        thrown = Assertions.assertThrows(
                AppointmentDateTimeException.class,
                () -> scheduleService.validateAppointmentDateTime(datetime3)
        );
        Assertions.assertTrue(thrown.message.contains("Out of operation time"));

        // After closing hour
        final var datetime4 = DateTime.now()
                .plusDays(1)
                .withTime(ScheduleService.closeTime.plusHours(1));
        thrown = Assertions.assertThrows(
                AppointmentDateTimeException.class,
                () -> scheduleService.validateAppointmentDateTime(datetime4)
        );
        Assertions.assertTrue(thrown.message.contains("Out of operation time"));
    }

    @Test
    public void testCheckPatientAvailability() {
        final var patientId = 0L;
        final var appointmentDatetime = DateTime.now()
                .withTime(ScheduleService.openTime.plusHours(1));
        Mockito.when(datetimeUtils.getMin(appointmentDatetime))
                .thenCallRealMethod();
        Mockito.when(datetimeUtils.getMax(appointmentDatetime))
                .thenCallRealMethod();

        // Available
        Mockito.when(appointmentRepo.existsAppointmentByPatientIdAndAppointmentTimeBetween(
                patientId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )).thenReturn(false);
        Assertions.assertDoesNotThrow(
                () -> scheduleService.checkPatientAvailability(appointmentDatetime, patientId)
        );

        // Not available
        Mockito.when(appointmentRepo.existsAppointmentByPatientIdAndAppointmentTimeBetween(
                patientId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )).thenReturn(true);
        final var thrown = Assertions.assertThrows(
                AppointmentConflictException.class,
                () -> scheduleService.checkPatientAvailability(appointmentDatetime, patientId)
        );
        Assertions.assertTrue(thrown.message.contains("Patient is occupied"));
    }

    @Test
    public void testCheckDoctorAvailability() {
        final var doctorId = 0L;
        final var appointmentDatetime = DateTime.now()
                .withTime(ScheduleService.openTime.plusHours(1));
        Mockito.when(datetimeUtils.getMin(appointmentDatetime))
                .thenCallRealMethod();
        Mockito.when(datetimeUtils.getMax(appointmentDatetime))
                .thenCallRealMethod();

        // Available
        Mockito.when(appointmentRepo.existsAppointmentByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )).thenReturn(false);
        Assertions.assertDoesNotThrow(
                () -> scheduleService.checkDoctorAvailability(appointmentDatetime, doctorId)
        );

        // Not available
        Mockito.when(appointmentRepo.existsAppointmentByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                datetimeUtils.getMin(appointmentDatetime).toDate(),
                datetimeUtils.getMax(appointmentDatetime).toDate()
        )).thenReturn(true);
        final var thrown = Assertions.assertThrows(
                AppointmentConflictException.class,
                () -> scheduleService.checkDoctorAvailability(appointmentDatetime, doctorId)
        );
        Assertions.assertTrue(thrown.message.contains("Doctor is occupied"));
    }
}
