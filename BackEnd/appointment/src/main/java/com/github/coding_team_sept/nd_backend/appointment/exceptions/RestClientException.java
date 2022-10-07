package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

public class RestClientException extends AppException {
    private RestClientException() {
        super(null, null);
    }

    private RestClientException(HttpStatus status, String message) {
        super(status, "Rest client exception: " + message);
    }

    public static RestClientException build(HttpStatus status, String message) {
        return new RestClientException(status, message);
    }

    public static RestClientException build(int status, String message) {
        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        var msg = message;
        try {
            httpStatus = HttpStatus.valueOf(status);
            final var response = (new ObjectMapper())
                    .readValue(
                            msg,
                            Map.class
                    );

            if (response.containsKey("error")) {
                msg = response.get("error").toString();
            } else if (response.containsKey("message")) {
                msg = response.get("message").toString();
            }
            return new RestClientException(httpStatus, msg);
        } catch (JsonProcessingException e) {
            return new RestClientException(
                    httpStatus,
                    (!msg.isEmpty()) ? msg : httpStatus.getReasonPhrase()
            );
        } catch (IllegalArgumentException e) {
            return new RestClientException(
                    httpStatus,
                    (!msg.isEmpty()) ? msg : "Invalid response status"
            );
        }
    }

    public static RestClientException fromRestClientResponseException(RestClientResponseException e) {
        return build(e.getRawStatusCode(), e.getResponseBodyAsString());
    }
}
