import com.github.coding_team_sept.nd_backend.chat.models.Chat;
import com.github.coding_team_sept.nd_backend.chat.models.Message;
import com.github.coding_team_sept.nd_backend.chat.payloads.requests.MessageRequest;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ValidateResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.DoctorAppointmentResponse;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.appointment.PatientAppointmentResponse;
import com.github.coding_team_sept.nd_backend.chat.repositories.ChatRepositories;
import com.github.coding_team_sept.nd_backend.chat.repositories.MessageRepositories;
import com.github.coding_team_sept.nd_backend.chat.services.AppointmentService;
import com.github.coding_team_sept.nd_backend.chat.services.AuthenticationService;
import com.github.coding_team_sept.nd_backend.chat.services.ChatService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChatServiceTest {
    @InjectMocks
    public ChatService chatService;
    @Mock
    ChatRepositories chatRepo;
    @Mock
    MessageRepositories msgRepo;
    @Mock
    AuthenticationService authService;
    @Mock
    AppointmentService appointmentService;

    public void mockGetAuthorization(ValidateResponse validateResponse) {
        Mockito.when(authService.getAuthorization(new HttpHeaders()))
                .thenReturn(validateResponse);
    }

    public void mockGetDoctorAppointment(
            DoctorAppointmentResponse doctorAppointmentResponse
    ) {
        Mockito.when(appointmentService.getDoctorAppointment(
                new HttpHeaders(),
                doctorAppointmentResponse.id
        )).thenReturn(doctorAppointmentResponse);
    }

    public void mockGetPatientAppointment(
            PatientAppointmentResponse patientAppointmentResponse
    ) {
        Mockito.when(appointmentService.getPatientAppointment(
                new HttpHeaders(),
                patientAppointmentResponse.id
        )).thenReturn(patientAppointmentResponse);
    }

    public void mockChatExistsById(Long id, boolean isExists) {
        Mockito.when(chatRepo.existsById(id))
                .thenReturn(isExists);
    }

    public void mockChatFindById(Chat chat) {
        Mockito.when(chatRepo.findById(chat.getAppointmentId()))
                .thenReturn(Optional.of(chat));
    }

    public void mockSaveMessage(Message msg) {
        Mockito.when(msgRepo.save(msg))
                .thenReturn(msg);
    }

    public void mockSaveAndFlushChat(Chat chat) {
        Mockito.when(chatRepo.saveAndFlush(chat))
                .thenReturn(chat);
    }

    public void mockFindAllMessagesByAppointmentId(
            Long id,
            List<Message> messages
    ) {
        Mockito.when(msgRepo.findAllByAppointmentId(id))
                .thenReturn(messages);
    }

    @Test
    public void testGetDoctorChatStatus() {
        final var fakeValidateResponse = new ValidateResponse(
                0L,
                "ROLE_DOCTOR"
        );

        final var fakeAppointmentResponse = new DoctorAppointmentResponse(
                fakeValidateResponse.id,
                UserDataResponse.build(
                        fakeValidateResponse.id + 1,
                        "patient.1@email.com",
                        "Patient One"
                ),
                LocalDateTime.now().toString(),
                "ONLINE"
        );

        mockGetAuthorization(fakeValidateResponse);
        mockGetDoctorAppointment(fakeAppointmentResponse);
        mockChatExistsById(fakeValidateResponse.id, true);
        mockChatFindById(Chat.builder()
                .doctorId(fakeValidateResponse.id)
                .patientId(fakeAppointmentResponse.appointedUser.id)
                .appointmentId(fakeAppointmentResponse.id)
                .doctorUR(0)
                .patientUR(0)
                .build()
        );

        Assertions.assertDoesNotThrow(
                () -> chatService.getChatStatus(new HttpHeaders(), 0L)
        );
    }

    @Test
    public void testGetPatientChatStatus() {
        final var fakeValidateResponse = new ValidateResponse(
                0L,
                "ROLE_PATIENT"
        );

        final var fakeAppointmentResponse = new PatientAppointmentResponse(
                fakeValidateResponse.id,
                UserDataResponse.build(
                        fakeValidateResponse.id + 1,
                        "doctor.1@email.com",
                        "Doctor One"
                ),
                LocalDateTime.now().toString(),
                "ONLINE"
        );

        mockGetAuthorization(fakeValidateResponse);
        mockGetPatientAppointment(fakeAppointmentResponse);
        mockChatExistsById(fakeAppointmentResponse.id, true);
        mockChatFindById(Chat.builder()
                .doctorId(fakeAppointmentResponse.appointedUser.id)
                .patientId(fakeValidateResponse.id)
                .appointmentId(fakeAppointmentResponse.id)
                .doctorUR(0)
                .patientUR(0)
                .lastMessageSid(0L)
                .build()
        );

        Assertions.assertDoesNotThrow(
                () -> chatService.getChatStatus(new HttpHeaders(), 0L)
        );
    }

    @Test
    public void testSendMessage() {
        final var fakeValidateResponse = new ValidateResponse(
                0L,
                "ROLE_PATIENT"
        );
        final var fakeChat = Chat.builder()
                .doctorId(fakeValidateResponse.id + 1)
                .patientId(fakeValidateResponse.id)
                .appointmentId(fakeValidateResponse.id)
                .doctorUR(0)
                .patientUR(0)
                .lastMessageSid(0L)
                .build();
        final var fakeMessageRequest = new MessageRequest(
                "Hello"
        );
        final var fakeMessage = Message.builder()
                .sid(fakeChat.getLastMessageSid() + 1)
                .appointmentId(fakeChat.getAppointmentId())
                .senderId(fakeValidateResponse.id)
                .message(fakeMessageRequest.message())
                .build();

        mockGetAuthorization(fakeValidateResponse);
        mockChatFindById(fakeChat);
        mockSaveMessage(fakeMessage);
        mockSaveAndFlushChat(fakeChat);

        Assertions.assertDoesNotThrow(
                () -> chatService.sendMessage(
                        new HttpHeaders(),
                        fakeChat.getAppointmentId(),
                        fakeMessageRequest
                )
        );
    }

    @Test
    public void testGetMessages() {
        final var fakeValidateResponse = new ValidateResponse(
                0L,
                "ROLE_PATIENT"
        );
        final var fakeChat = Chat.builder()
                .doctorId(fakeValidateResponse.id + 1)
                .patientId(fakeValidateResponse.id)
                .appointmentId(0L)
                .doctorUR(0)
                .patientUR(1)
                .lastMessageSid(0L)
                .build();
        final var fakeMessages = List.of(
                Message.builder()
                        .sid(fakeChat.getLastMessageSid())
                        .appointmentId(fakeChat.getAppointmentId())
                        .message("Hello")
                        .senderId(fakeChat.getDoctorId())
                        .build()
        );

        mockGetAuthorization(fakeValidateResponse);
        mockChatFindById(fakeChat);
        mockFindAllMessagesByAppointmentId(
                fakeChat.getAppointmentId(),
                fakeMessages
        );
        chatRepo.save(fakeChat);

        Assertions.assertDoesNotThrow(
                () -> {
                    final var result = chatService.getMessages(
                            new HttpHeaders(),
                            fakeChat.getAppointmentId(),
                            false
                    );
                    Assertions.assertEquals(fakeMessages.size(), result.size());
                }
        );
    }
}
