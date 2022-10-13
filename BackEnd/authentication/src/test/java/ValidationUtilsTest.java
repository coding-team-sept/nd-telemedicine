import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.EmailFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.PasswordFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.UserNameFormatException;
import com.github.coding_team_sept.nd_backend.authentication.utils.ValidationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ValidationUtilsTest {
    private final List<TestCase> emailTestCases = List.of(
            TestCase.build("admin@admin.com", true),
            TestCase.build("admin.1@admin.com.au", true),
            TestCase.build("@admin.com", false),
            TestCase.build("admin.com", false),
            TestCase.build("admin@admin.", false),
            TestCase.build("admin@admin", false),
            TestCase.build("admin@admin.c", false),
            TestCase.build("admin@admin.toolong", false),
            TestCase.build("", false)
    );

    private final List<TestCase> usernameTestCases = List.of(
            TestCase.build("Admin One", true),
            TestCase.build("Admin-One.Two,Three's", true),
            TestCase.build("Admin 1", false),
            TestCase.build("a", false),
            TestCase.build("", false)
    );

    private final List<TestCase> passwordTestCases = List.of(
            TestCase.build("AdminOne", true),
            TestCase.build("Admin", false)
    );

    @Test
    public void testValidateEmail() {
        emailTestCases.forEach(testCase -> {
            Assertions.assertEquals(
                    testCase.ok,
                    ValidationUtils.validateEmail(testCase.testCase));
        });
    }

    @Test
    public void testValidateEmailElseThrow() {
        emailTestCases.forEach(testCase -> {
            if (!testCase.ok) {
                Assertions.assertThrows(
                        EmailFormatException.class,
                        () -> ValidationUtils.validateEmailElseThrow(testCase.testCase)
                );
            }
        });
    }

    @Test
    public void testValidateUsername() {
        usernameTestCases.forEach(testCase -> {
            Assertions.assertEquals(
                    testCase.ok,
                    ValidationUtils.validateUserName(testCase.testCase));
        });
    }

    @Test
    public void testValidateUsernameElseThrow() {
        usernameTestCases.forEach(testCase -> {
            if (!testCase.ok) {
                Assertions.assertThrows(
                        UserNameFormatException.class,
                        () -> ValidationUtils.validateUserNameElseThrow(testCase.testCase)
                );
            }
        });
    }

    @Test
    public void testValidatePassword() {
        passwordTestCases.forEach(testCase -> {
            Assertions.assertEquals(
                    testCase.ok,
                    ValidationUtils.validatePassword(testCase.testCase));
        });
    }

    @Test
    public void testValidatePasswordElseThrow() {
        passwordTestCases.forEach(testCase -> {
            if (!testCase.ok) {
                Assertions.assertThrows(
                        PasswordFormatException.class,
                        () -> ValidationUtils.validatePasswordElseThrow(testCase.testCase)
                );
            }
        });
    }

    public record TestCase(
            String testCase,
            boolean ok
    ) {
        public static TestCase build(
                String testCase,
                boolean ok
        ) {
            return new TestCase(testCase, ok);
        }
    }
}
