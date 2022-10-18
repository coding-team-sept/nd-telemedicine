import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ValidateResponse;
import com.github.coding_team_sept.nd_backend.appointment.services.AuthenticationService;
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

import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testGetAuthorizationService() {
        final var expectedResponse = new ValidateResponse(1L, "ROLE_PATIENT");
        final var header = new HttpHeaders();
        Mockito.when(restTemplate.exchange(
                AuthenticationService.url + "/auth/validate",
                HttpMethod.GET,
                new HttpEntity<>(header),
                ValidateResponse.class
        )).thenReturn(ResponseEntity.ok(expectedResponse));

        final var actualResponse = authenticationService.getAuthorization(header);
        Assertions.assertEquals(expectedResponse.id, actualResponse.id);
        Assertions.assertEquals(expectedResponse.role, actualResponse.role);
    }

    @Test
    public void testGetAllDoctorUsers() {
        final var header = new HttpHeaders();
        final var expectedResponse = UsersDataResponse.build(
                List.of(
                        UserDataResponse.build(0L, "one@doctor.com", "Doctor One"),
                        UserDataResponse.build(1L, "two@doctor.com", "Doctor Two"),
                        UserDataResponse.build(2L, "three@doctor.com", "Doctor Three")
                )
        );
        Mockito.when(restTemplate.exchange(
                AuthenticationService.url + "/app/admin/doctor",
                HttpMethod.GET,
                new HttpEntity<>(header),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        )).thenReturn(ResponseEntity.ok(ResponseWrapper.fromData(expectedResponse)));

        final var actualResponse = authenticationService.getUsers(header, "doctor");
        Assertions.assertEquals(expectedResponse.users.size(), actualResponse.users.size());
        for (int i = 0; i < expectedResponse.users.size(); i++) {
            final var expectedUser = expectedResponse.users.get(i);
            final var actualUser = actualResponse.users.get(i);
            Assertions.assertEquals(expectedUser.id, actualUser.id);
            Assertions.assertEquals(expectedUser.name, actualUser.name);
            Assertions.assertEquals(expectedUser.email, actualUser.email);
        }
    }

    @Test
    public void testGetAllPatientUsers() {
        final var header = new HttpHeaders();
        final var expectedResponse = UsersDataResponse.build(
                List.of(
                        UserDataResponse.build(0L, "one@patient.com", "Patient One"),
                        UserDataResponse.build(1L, "two@patient.com", "Patient Two"),
                        UserDataResponse.build(2L, "three@patient.com", "Patient Three")
                )
        );
        Mockito.when(restTemplate.exchange(
                AuthenticationService.url + "/app/admin/patient",
                HttpMethod.GET,
                new HttpEntity<>(header),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        )).thenReturn(ResponseEntity.ok(ResponseWrapper.fromData(expectedResponse)));

        final var actualResponse = authenticationService.getUsers(header, "patient");
        Assertions.assertEquals(expectedResponse.users.size(), actualResponse.users.size());
        for (int i = 0; i < expectedResponse.users.size(); i++) {
            final var expectedUser = expectedResponse.users.get(i);
            final var actualUser = actualResponse.users.get(i);
            Assertions.assertEquals(expectedUser.id, actualUser.id);
            Assertions.assertEquals(expectedUser.name, actualUser.name);
            Assertions.assertEquals(expectedUser.email, actualUser.email);
        }
    }

    @Test
    public void testGetDoctorUsersBasedOnId() {
        final var header = new HttpHeaders();
        final var expectedResponse = UsersDataResponse.build(
                List.of(
                        UserDataResponse.build(0L, "one@doctor.com", "Doctor One"),
                        UserDataResponse.build(1L, "two@doctor.com", "Doctor Two"),
                        UserDataResponse.build(2L, "three@doctor.com", "Doctor Three")
                )
        );

        final var ids = expectedResponse.users.stream()
                .map(userDataResponse -> userDataResponse.id)
                .toList();

        final var uri = UriComponentsBuilder.fromHttpUrl(
                        AuthenticationService.url + "/app/admin/doctor"
                ).queryParam("ids", ids)
                .encode()
                .toUriString();

        Mockito.when(restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(header),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        )).thenReturn(ResponseEntity.ok(ResponseWrapper.fromData(expectedResponse)));

        final var actualResponse = authenticationService.getUsers(
                header,
                ids,
                "doctor"
        );

        Assertions.assertEquals(expectedResponse.users.size(), actualResponse.users.size());
        for (int i = 0; i < expectedResponse.users.size(); i++) {
            final var expectedUser = expectedResponse.users.get(i);
            final var actualUser = actualResponse.users.get(i);
            Assertions.assertEquals(expectedUser.id, actualUser.id);
            Assertions.assertEquals(expectedUser.name, actualUser.name);
            Assertions.assertEquals(expectedUser.email, actualUser.email);
        }
    }

    @Test
    public void testGetPatientUsersBasedOnId() {
        final var header = new HttpHeaders();
        final var expectedResponse = UsersDataResponse.build(
                List.of(
                        UserDataResponse.build(0L, "one@patient.com", "Patient One"),
                        UserDataResponse.build(1L, "two@patient.com", "Patient Two"),
                        UserDataResponse.build(2L, "three@patient.com", "Patient Three")
                )
        );

        final var ids = expectedResponse.users.stream()
                .map(userDataResponse -> userDataResponse.id)
                .toList();

        final var uri = UriComponentsBuilder.fromHttpUrl(AuthenticationService.url + "/app/admin/patient")
                .queryParam("ids", ids)
                .encode()
                .toUriString();

        Mockito.when(restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(header),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        )).thenReturn(ResponseEntity.ok(ResponseWrapper.fromData(expectedResponse)));

        final var actualResponse = authenticationService.getUsers(
                header,
                ids,
                "patient"
        );
        Assertions.assertEquals(expectedResponse.users.size(), actualResponse.users.size());
        for (int i = 0; i < expectedResponse.users.size(); i++) {
            final var expectedUser = expectedResponse.users.get(i);
            final var actualUser = actualResponse.users.get(i);
            Assertions.assertEquals(expectedUser.id, actualUser.id);
            Assertions.assertEquals(expectedUser.name, actualUser.name);
            Assertions.assertEquals(expectedUser.email, actualUser.email);
        }
    }
}
