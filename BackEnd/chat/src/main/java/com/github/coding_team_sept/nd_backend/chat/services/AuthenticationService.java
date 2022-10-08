package com.github.coding_team_sept.nd_backend.chat.services;

import com.github.coding_team_sept.nd_backend.chat.exceptions.RestClientException;
import com.github.coding_team_sept.nd_backend.chat.payloads.responses.ValidateResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public record AuthenticationService(
        RestTemplate restTemplate
) {
    public static final String url = "http://localhost:9000/api/v1";

    public ValidateResponse getAuthorization(
            HttpHeaders headers
    ) throws RestClientException {
        try {
            // Check "Authorization"
            final var response = restTemplate.exchange(
                    url + "/auth/validate",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ValidateResponse.class
            );

            if (response.getBody() != null) {
                return response.getBody();
            }
            throw RestClientException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Empty response body");
        } catch (RestClientResponseException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
    }
}
