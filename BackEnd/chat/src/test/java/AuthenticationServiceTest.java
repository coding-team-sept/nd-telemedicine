import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ValidateResponse;
import com.github.coding_team_sept.nd_backend.chat.services.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    private void mockGetAuthorizationRestTemplate(ValidateResponse validateResponse) {
        Mockito.when(restTemplate.exchange(
                AuthenticationService.url + "/auth/validate",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                ValidateResponse.class
        )).thenReturn(ResponseEntity.ok(validateResponse));
    }

    @Test
    public void testGetAuthorization() {
        final var fakeValidateResponse = new ValidateResponse(0L, "ROLE_PATIENT");
        mockGetAuthorizationRestTemplate(fakeValidateResponse);
        Assertions.assertDoesNotThrow(() -> authenticationService.getAuthorization(new HttpHeaders()));
    }
}
