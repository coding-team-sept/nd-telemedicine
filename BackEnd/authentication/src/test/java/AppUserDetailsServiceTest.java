import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppUserDetailsServiceTest {
    @Mock
    private AppUserRepository appUserRepo;
    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    private void mockFindUserByEmail(AppUser appUser, boolean isSuccessful) {
        Mockito.when(appUserRepo.findUserByEmail(appUser.getEmail()))
                .thenReturn((isSuccessful) ? Optional.of(appUser) : Optional.empty());
    }

    @Test
    public void testLoadUserByEmailSuccessful() {
        final var fakeAppUser = AppUser.builder()
                .id(0L)
                .name("Admin One")
                .role(Role.builder().id(0L).name(RoleType.ROLE_ADMIN).build())
                .password("AdminOne")
                .email("admin.one@email.com")
                .build();
        mockFindUserByEmail(fakeAppUser, true);

        Assertions.assertDoesNotThrow(() -> appUserDetailsService.loadUserByUsername(fakeAppUser.getEmail()));
        Assertions.assertDoesNotThrow(() -> appUserDetailsService.loadUserByEmail(fakeAppUser.getEmail()));
    }

    @Test
    public void testLoadUserByEmailThenUserNotFound() {
        final var fakeAppUser = AppUser.builder()
                .id(0L)
                .name("Admin One")
                .role(Role.builder().id(0L).name(RoleType.ROLE_ADMIN).build())
                .password("AdminOne")
                .email("admin.one@email.com")
                .build();
        mockFindUserByEmail(fakeAppUser, false);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> appUserDetailsService.loadUserByUsername(fakeAppUser.getEmail()));
        Assertions.assertThrows(UsernameNotFoundException.class, () -> appUserDetailsService.loadUserByEmail(fakeAppUser.getEmail()));
    }
}
