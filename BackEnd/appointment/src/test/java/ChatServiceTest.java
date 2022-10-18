import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.services.ChatService;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChatServiceTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ChatService chatService;

    private void mockGetChatStatusRestTemplate(Long appointmentId) {
        Mockito.when(
                restTemplate.exchange(
                        (ChatService.url + "/chat/status/" + appointmentId),
                        HttpMethod.GET,
                        new HttpEntity<>(new HttpHeaders()),
                        new ParameterizedTypeReference<ResponseWrapper<ChatResponse>>() {
                        }
                )
        ).thenReturn(ResponseEntity.ok(ResponseWrapper.fromData(new ChatResponse())));
    }

    @Test
    public void testGetChatStatus() {
        mockGetChatStatusRestTemplate(0L);

        Assertions.assertDoesNotThrow(() -> chatService.getChatStatus(new HttpHeaders(), 0L));
    }
}
