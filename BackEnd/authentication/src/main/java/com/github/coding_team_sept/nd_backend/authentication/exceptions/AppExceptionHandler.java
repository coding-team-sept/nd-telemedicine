package com.github.coding_team_sept.nd_backend.authentication.exceptions;

import com.github.coding_team_sept.nd_backend.authentication.payloads.responses.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AppException.class)
    protected ResponseEntity<ErrorResponse> handleAppException(AppException e) {
        return new ResponseEntity<>(ErrorResponse.fromException(e), e.status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.fromUnknownException(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> handleDataIntegrityException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(ErrorResponse.build("Email has been taken"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(ErrorResponse.fromException(e), HttpStatus.BAD_REQUEST);
    }
}
