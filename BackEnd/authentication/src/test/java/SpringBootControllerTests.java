import com.github.coding_team_sept.nd_backend.authentication.controllers.AuthenticationController;
import com.github.coding_team_sept.nd_backend.authentication.enums.RoleType;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUser;
import com.github.coding_team_sept.nd_backend.authentication.models.Role;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.LoginRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.requests.RegisterRequest;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AppResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.AuthResponse;
import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.UserDataResponse;
import com.github.coding_team_sept.nd_backend.authentication.repositories.AppUserRepository;
import com.github.coding_team_sept.nd_backend.authentication.services.AppUserService;
import com.github.coding_team_sept.nd_backend.authentication.services.AuthenticationService;
import org.apache.catalina.User;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class SpringBootControllerTests {

    @Autowired
    AuthenticationService authService;
    @Autowired
    MockMvc mockMvc;
    @Test
    void loginTest() throws AppException {
        authService.login(new LoginRequest("admin@admin.com", "password"));
        AuthResponse responseEntity = authService.login(new LoginRequest("admin@admin.com","password"));


     //   assertEquals(3, empList.size());
       // verify(dao, times(1)).findAll();

        //RegisterRequest registerRequest = new RegisterRequest("admin@admin.com", "admin", "password");
        //ResponseEntity responseEntity = authController.addAdmin(registerRequest);
        //assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
  //  @InjectMocks


    @MockBean
    private AppUserService service;
    @Test
    void getUserByRoleTest(){
        List<UserDataResponse> list = new ArrayList<UserDataResponse>();
        service.getUserByRole(RoleType.ROLE_ADMIN);
        User user1 = new UserDataResponse("1", "admin@admin.com", "admin");

       // when(repository.findAppUserByRole(new Role())).thenReturn();
        //assertEquals(2, service.getUserByRole(RoleType.RO  LE_ADMIN).size());
    }
    @Test
    void getUserByIdTest(){
        service.getUserById(RoleType.ROLE_ADMIN);



    }
    @Test
    void getUserByIdsTest(){
        List<UserDataResponse> list = new ArrayList<UserDataResponse>();

    }
    @Test
    void addAdminTest(){

    }
    @Test
    void addDoctorTest(){

    }
    @Test
    void registerPatientTest(){

    }

}
