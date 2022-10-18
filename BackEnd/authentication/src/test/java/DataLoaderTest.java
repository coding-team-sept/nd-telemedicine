import com.github.coding_team_sept.nd_backend.authentication.components.DataLoader;
import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DataLoaderTest {
    @Mock
    private RoleRepository roleRepo;
    @Mock
    private AppUserRepository appUserRepo;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private DataLoader dataLoader;

    private void mockFindRoleByName(Role role, boolean isSuccessful) {
        Mockito.when(roleRepo.findRoleByName(role.getName()))
                .thenReturn((isSuccessful) ? Optional.of(role) : Optional.empty());
    }

    private void mockSaveRole(Role role) {
        Mockito.when(roleRepo.save(role))
                .thenReturn(role);
    }

    private void mockFindUserByEmail(AppUser appUser, boolean isSuccessful) {
        Mockito.when(appUserRepo.findUserByEmail(appUser.getEmail()))
                .thenReturn((isSuccessful) ? Optional.of(appUser) : Optional.empty());
    }

    private void mockSaveUser(AppUser appUser) {
        Mockito.when(appUserRepo.save(appUser))
                .thenReturn(appUser);
    }

    @Test
    public void testCreateRole() {
        final var fakeRole = Role.builder()
                .id(0L)
                .name(RoleType.ROLE_ADMIN)
                .build();
        mockFindRoleByName(fakeRole, true);
        mockSaveRole(fakeRole);
        Assertions.assertDoesNotThrow(() ->dataLoader.createRole(fakeRole.getName()));
    }

    @Test
    public void testCreateAdmin() {
        final var fakeAppUser = AppUser
                .builder()
                .name("Admin")
                .email("admin@admin.com")
                .password(encoder.encode("admin123"))
                .role(roleRepo.findRoleByName(RoleType.ROLE_ADMIN).orElse(null))
                .build();
        mockFindUserByEmail(fakeAppUser, true);
        mockSaveUser(fakeAppUser);
        Assertions.assertDoesNotThrow(() -> dataLoader.createAdmin());
    }
}
