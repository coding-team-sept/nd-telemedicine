import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.utils.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtUtilsTest {
    JwtUtils jwtUtils = new JwtUtils();

    AppUserDetails generateFakeAppUserDetails() {
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

    @Test
    public void testValidJwt() {
        final var fakeAppUserDetails = generateFakeAppUserDetails();
        final var token = jwtUtils.generateToken(fakeAppUserDetails);

        Assertions.assertTrue(jwtUtils.validateToken(token));
        Assertions.assertEquals(fakeAppUserDetails.getEmail(), jwtUtils.extractEmailFromToken(token));
        Assertions.assertEquals(fakeAppUserDetails.getId(), jwtUtils.extractIdFromToken(token));
        Assertions.assertEquals(
                fakeAppUserDetails.getRole().getName().toString(),
                jwtUtils.extractRoleFromToken(token)
        );
    }

    @Test
    public void testInvalidJwt() {
        final var invalidToken = "invalid jwt";

        Assertions.assertFalse(() -> jwtUtils.validateToken(invalidToken));
        Assertions.assertThrows(Exception.class, () -> jwtUtils.extractEmailFromToken(invalidToken));
        Assertions.assertThrows(Exception.class, () -> jwtUtils.extractIdFromToken(invalidToken));
        Assertions.assertThrows(Exception.class, () -> jwtUtils.extractRoleFromToken(invalidToken));
    }
}
