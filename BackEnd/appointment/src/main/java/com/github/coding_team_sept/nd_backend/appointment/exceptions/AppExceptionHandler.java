package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import com.github.coding_team_sept.nd_backend.appointment.payload.responses.ErrorResponse;
import org.junit.jupiter.api.Order;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        e.printStackTrace();
        return new ResponseEntity<>(ErrorResponse.fromUnknownException(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
