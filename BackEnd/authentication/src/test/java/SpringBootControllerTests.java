import com.github.coding_team_sept.nd_backend.authentication.controllers.AuthenticationController;
import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class SpringBootControllerTests {

    @Test
    void addUserTest(){
        AuthenticationController authController = new AuthenticationController();
        RegisterRequest registerRequest = new RegisterRequest("admin@admin.com", "admin", "password");
        ResponseEntity responseEntity = authController.addAdmin(registerRequest);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final var responseBody = responseEntity.getBody();
        assertThat(responseEntity.name).isEqual(name);
        assertThat(responseEntity.password).isEqual(password);
        assertThat(responseEntity.email).isEqual(RoleType.ROLE_ADMIN);
    }

}
