package com.github.coding_team_sept.nd_backend.authentication.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPointJwt is used to handle the authentication error.
 * It will only be used in SecurityConfig
 *
 * @author nivratig
 * */
@Slf4j
@Component
public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e
    ) throws IOException, ServletException {
        log.error("Unauthorized: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
