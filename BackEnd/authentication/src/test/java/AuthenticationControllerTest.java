import com.github.coding_team_sept.nd_backend.authentication.controllers.AuthenticationController;
import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testValidate() {
        final var fakeAppUserDetails = AppUserDetails.builder()
                .id(0L)
                .name("Admin One")
                .email("admin.1@example.com")
                .role(Role.builder()
                        .id(0L)
                        .name(RoleType.ROLE_ADMIN)
                        .build())
                .password("AdminOne")
                .build();
        final var fakeAuth = new UsernamePasswordAuthenticationToken(
                fakeAppUserDetails,
                fakeAppUserDetails.getPassword()
        );

        final var securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication())
                        .thenReturn(fakeAuth);
        SecurityContextHolder.setContext(securityContext);

        Assertions.assertDoesNotThrow(() -> authenticationController.validate());
    }
}
