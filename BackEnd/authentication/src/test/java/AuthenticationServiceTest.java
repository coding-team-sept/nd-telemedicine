import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.EmailTakenException;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthenticationServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtUtils jwtUtils;
    @InjectMocks
    private AuthenticationService authenticationService;

    private Role getUserRoleFromRoleType(RoleType roleType) {
        return Role.builder()
                .id(Integer.valueOf(roleType.ordinal()).longValue())
                .name(roleType)
                .build();
    }

    private LoginRequest getFakeLoginRequest() {
        return new LoginRequest(
                "admin.1@example.com",
                "AdminOne"
        );
    }

    private RegisterRequest getFakeRegisterRequest() {
        return new RegisterRequest(
                "admin.1@example.com",
                "Admin One",
                "AdminOne"
        );
    }

    private AppUserDetails generateFakeAppUserDetails() {
        return AppUserDetails.builder()
                .id(0L)
                .name("Admin One")
                .email("admin.1@example.com")
                .role(Role.builder()
                        .id(0L)
                        .name(RoleType.ROLE_ADMIN)
                        .build())
                .password("AdminOne")
                .build();
    }

    // Mock authentication manager
    private void mockAuthenticationManager(UsernamePasswordAuthenticationToken authentication) {
        Mockito.when(authenticationManager.authenticate(authentication))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        generateFakeAppUserDetails(),
                        authentication.getCredentials()
                ));
    }

    // Mock app user repo
    private void mockExistsAppUserByEmail(
            String email,
            boolean isExists
    ) {
        Mockito.when(appUserRepository.existsAppUserByEmail(email))
                .thenReturn(isExists);
    }

    private void mockSaveUser(AppUser appUser) {
        Mockito.when(appUserRepository.save(appUser))
                .thenReturn(appUser);
    }

    // Mock encoder
    private void mockEncoder(String password) {
        Mockito.when(encoder.encode(password))
                .thenReturn(new BCryptPasswordEncoder().encode(password));
    }

    // Mock jwt utils
    private void mockGenerateToken(AppUserDetails appUserDetails) {
        Mockito.when(jwtUtils.generateToken(appUserDetails))
                .thenCallRealMethod();
    }

    // Mock role repo
    private void mockFindRoleByName() {
        for (var roleType : RoleType.values()) {
            Mockito.when(roleRepository.findRoleByName(roleType))
                    .thenReturn(Optional.of(getUserRoleFromRoleType(roleType)));
        }
    }

    @Test
    public void testLogin() {
        final var fakeLoginRequest = getFakeLoginRequest();
        mockAuthenticationManager(new UsernamePasswordAuthenticationToken(fakeLoginRequest.email(), fakeLoginRequest.password()));

        final var result = authenticationService.login(fakeLoginRequest);
        Assertions.assertEquals(fakeLoginRequest.email(), result.user.email);
    }


    @Test
    public void testRegisterSuccessful() {
        final var fakeRegisterRequest = getFakeRegisterRequest();
        final var roleType = RoleType.ROLE_ADMIN;
        final var fakeAppUser = AppUser.builder()
                .email(fakeRegisterRequest.email())
                .name(fakeRegisterRequest.name())
                .password(encoder.encode(fakeRegisterRequest.password()))
                .role(
                        Role.builder()
                                .id(0L)
                                .name(roleType)
                                .build()
                )
                .build();
        mockExistsAppUserByEmail(fakeRegisterRequest.email(), false);
        mockEncoder(fakeRegisterRequest.password());
        mockFindRoleByName();
        mockSaveUser(fakeAppUser);
        mockGenerateToken(AppUserDetails.fromAppUser(fakeAppUser));

        final var result = authenticationService.register(
                fakeRegisterRequest,
                roleType
        );
        Assertions.assertNotNull(result.token.access);
    }

    @Test
    public void testRegisterThenUserExists() {
        final var fakeRegisterRequest = getFakeRegisterRequest();
        final var roleType = RoleType.ROLE_ADMIN;
        mockExistsAppUserByEmail(fakeRegisterRequest.email(), true);
        Assertions.assertThrows(
                EmailTakenException.class,
                () -> authenticationService.register(
                        fakeRegisterRequest,
                        roleType
                )
        );
    }
}
