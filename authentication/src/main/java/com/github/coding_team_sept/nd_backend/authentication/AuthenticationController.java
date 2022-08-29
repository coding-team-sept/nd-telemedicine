package com.github.coding_team_sept.nd_backend.authentication;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUserRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/user")
public record AuthenticationController(AuthenticationService service) {

    @PostMapping
    public void register(@RequestBody AppUserRegistrationRequest request) {
        service.register(request);
        log.info("New user registration: {}!", request);
    }

}
