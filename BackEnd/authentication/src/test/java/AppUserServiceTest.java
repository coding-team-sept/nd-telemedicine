import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.repositories.RoleRepository;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepo;
    @Mock
    private RoleRepository roleRepo;
    @InjectMocks
    private AppUserService appUserService;

    private static Role getUserRoleFromRoleType(RoleType roleType) {
        return Role.builder()
                .id(Integer.valueOf(roleType.ordinal()).longValue())
                .name(roleType)
                .build();
    }

    @BeforeEach
    public void setup() {
        mockFindRoleByName();
    }

    private void mockFindRoleByName() {
        for (var roleType : RoleType.values()) {
            Mockito.when(roleRepo.findRoleByName(roleType))
                    .thenReturn(Optional.of(getUserRoleFromRoleType(roleType)));
        }
    }

    private void mockFindAppUserByRole(List<AppUser> appUsers, RoleType roleType) {
        final var filteredAppUsers = appUsers.stream()
                .filter(appUser -> appUser.getRole().getName().equals(roleType))
                .toList();
        Mockito.when(appUserRepo.findAppUserByRole(getUserRoleFromRoleType(roleType)))
                .thenReturn(Optional.of(filteredAppUsers));
    }

    private void mockFindAppUserById(AppUser appUser) {
        Mockito.when(appUserRepo.findById(appUser.getId()))
                .thenReturn(Optional.of(appUser));
    }

    private void mockFindAppUserByIdAndRole(AppUser appUser) {
        Mockito.when(appUserRepo.findAppUserByIdAndRole(appUser.getId(), appUser.getRole()))
                .thenReturn(Optional.of(appUser));
    }

    private List<AppUser> generateAppUsers() {
        return List.of(
                AppUser.builder()
                        .id(0L)
                        .name("Patient One")
                        .password("PatientOne")
                        .email("patient.one@email.com")
                        .role(getUserRoleFromRoleType(RoleType.ROLE_PATIENT))
                        .build()
                ,
                AppUser.builder()
                        .id(0L)
                        .name("Patient Two")
                        .password("PatientTwo")
                        .email("patient.two@email.com")
                        .role(getUserRoleFromRoleType(RoleType.ROLE_PATIENT))
                        .build()
                ,
                AppUser.builder()
                        .id(0L)
                        .name("Doctor One")
                        .password("DoctorOne")
                        .email("doctor.one@email.com")
                        .role(getUserRoleFromRoleType(RoleType.ROLE_DOCTOR))
                        .build()
        );
    }

    @Test
    public void testGetUserByRole() {
        final var fakeAppUsers = generateAppUsers();
        mockFindRoleByName();
        mockFindAppUserByRole(fakeAppUsers, RoleType.ROLE_PATIENT);
        mockFindAppUserByRole(fakeAppUsers, RoleType.ROLE_DOCTOR);

        final var rolePatientResult = appUserService.getUserByRole(RoleType.ROLE_PATIENT);
        final var roleDoctorResult = appUserService.getUserByRole(RoleType.ROLE_DOCTOR);
        Assertions.assertEquals(2, rolePatientResult.size());
        Assertions.assertEquals(1, roleDoctorResult.size());
    }

    @Test
    public void getUsersByIds() {
        final var fakeAppUsers = generateAppUsers();
        mockFindRoleByName();

        final var rolePatientResult = appUserService.getUsersByIds(
                fakeAppUsers.stream()
                        .filter(appUser -> appUser.getRole().getName().equals(RoleType.ROLE_PATIENT))
                        .map(appUser -> {
                            mockFindAppUserById(appUser);
                            mockFindAppUserByIdAndRole(appUser);
                            return appUser.getId();
                        })
                        .toList(),
                RoleType.ROLE_PATIENT
        );

        final var roleDoctorResult = appUserService.getUsersByIds(
                fakeAppUsers.stream()
                        .filter(appUser -> appUser.getRole().getName().equals(RoleType.ROLE_DOCTOR))
                        .map(appUser -> {
                            mockFindAppUserById(appUser);
                            mockFindAppUserByIdAndRole(appUser);
                            return appUser.getId();
                        })
                        .toList(),
                RoleType.ROLE_DOCTOR
        );

        Assertions.assertEquals(2, rolePatientResult.size());
        Assertions.assertEquals(1, roleDoctorResult.size());
    }
}
