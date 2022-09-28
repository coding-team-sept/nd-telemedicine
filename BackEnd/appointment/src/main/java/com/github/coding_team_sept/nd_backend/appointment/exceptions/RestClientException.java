package com.github.coding_team_sept.nd_backend.appointment.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

public class RestClientException extends AppException {
    public RestClientException(HttpStatus status, String message) {
        super(status, "Rest client exception: " + message);
    }

    public static RestClientException fromRawString(int status, String message) {
        try {
            String m = null;
            final var response = (new ObjectMapper())
                    .readValue(
                            message,
                            Map.class
                    );
            if (response.containsKey("error")) {
                m = response.get("error").toString();
            } else if (response.containsKey("message")) {
                m = response.get("message").toString();
            }
            return new RestClientException(HttpStatus.valueOf(status), m);
        } catch (JsonProcessingException e) {
            return new RestClientException(HttpStatus.valueOf(status), message);
        }
    }

    public static RestClientException fromRestClientResponseException(RestClientResponseException e) {
        return fromRawString(e.getRawStatusCode(), e.getResponseBodyAsString());
    }
}
