import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.AppointmentsResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.chat.services.AppointmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppointmentServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AppointmentService appointmentService;

    private void mockGetDoctorAppointmentRestTemplate(
            Long id,
            List<DoctorAppointmentResponse> doctorAppointmentResponses
    ) {
        Mockito.when(restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(AppointmentService.url + "/app/doctor/appointment")
                        .queryParam("id", id)
                        .encode()
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<ResponseWrapper<AppointmentsResponse<DoctorAppointmentResponse>>>() {
                }
        )).thenReturn(ResponseEntity.ok(
                ResponseWrapper.fromData(
                        AppointmentsResponse.build(List.of(doctorAppointmentResponses.get(id.intValue())))
                )
        ));
    }

    private void mockGetPatientAppointmentRestTemplate(
            Long id,
            List<PatientAppointmentResponse> patientAppointmentResponses
    ) {
        Mockito.when(restTemplate.exchange(
                UriComponentsBuilder.fromHttpUrl(AppointmentService.url + "/app/patient/appointment")
                        .queryParam("id", id)
                        .encode()
                        .toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<ResponseWrapper<AppointmentsResponse<PatientAppointmentResponse>>>() {
                }
        )).thenReturn(ResponseEntity.ok(
                ResponseWrapper.fromData(
                        AppointmentsResponse.build(List.of(patientAppointmentResponses.get(id.intValue())))
                )
        ));
    }

    @Test
    public void testGetDoctorAppointment() {
        final var id = 2L;
        final var fakeAppointments = Stream.of(0, 1, 2)
                .map(integer -> new DoctorAppointmentResponse(
                        integer.longValue(),
                        UserDataResponse.build(
                                integer.longValue(),
                                "patient." + integer + "@email.com",
                                "Patient"
                        ),
                        LocalDateTime.now().toString(),
                        "OFFLINE"
                )).toList();
        mockGetDoctorAppointmentRestTemplate(id, fakeAppointments);
        final var result = appointmentService.getDoctorAppointment(new HttpHeaders(), id);

        Assertions.assertEquals(fakeAppointments.get((int) id).appointedUser.email, result.getAppointedUser().email);
    }

    @Test
    public void testGetPatientAppointment() {
        final var id = 2L;
        final var fakeAppointments = Stream.of(0, 1, 2)
                .map(integer -> new PatientAppointmentResponse(
                        integer.longValue(),
                        UserDataResponse.build(
                                integer.longValue(),
                                "doctor." + integer + "@email.com",
                                "Doctor"
                        ),
                        LocalDateTime.now().toString(),
                        "OFFLINE"
                )).toList();
        mockGetPatientAppointmentRestTemplate(id, fakeAppointments);
        final var result = appointmentService.getPatientAppointment(new HttpHeaders(), id);

        Assertions.assertEquals(fakeAppointments.get((int) id).appointedUser.email, result.getAppointedUser().email);
    }
}
