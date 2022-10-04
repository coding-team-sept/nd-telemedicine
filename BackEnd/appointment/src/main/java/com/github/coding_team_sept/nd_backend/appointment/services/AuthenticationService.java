package com.github.coding_team_sept.nd_backend.appointment.services;

import com.github.coding_team_sept.nd_backend.appointment.exceptions.RestClientException;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ValidateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    RestTemplate restTemplate;

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

    public UsersDataResponse getUsers(
            HttpHeaders headers,
            List<Long> ids,
            String target
    ) throws RestClientException {
        try {
            String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/admin/" + target)
                    .queryParam("ids", ids)
                    .encode()
                    .toUriString();

            final var response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                    }
            );

            if (response.getBody() != null && response.getBody().data.users != null) {
                return response.getBody().data;
            }
        } catch (HttpClientErrorException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
        return UsersDataResponse.build(List.of());
    }

    public UsersDataResponse getUsers(
            HttpHeaders headers,
            String target
    ) throws RestClientException {
        try {
            final var response = restTemplate.exchange(
                    AuthenticationService.url + "/app/admin/" + target,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                    }
            );

            if (response.getBody() != null && response.getBody().data.users != null) {
                return response.getBody().data;
            }
        } catch (RestClientResponseException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
        return UsersDataResponse.build(List.of());
    }
}
