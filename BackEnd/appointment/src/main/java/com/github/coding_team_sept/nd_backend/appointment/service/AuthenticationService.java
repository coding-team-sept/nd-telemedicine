package com.github.coding_team_sept.nd_backend.appointment.service;

import com.github.coding_team_sept.nd_backend.appointment.payload.responses.ResponseWrapper;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.UsersDataResponse;
import com.github.coding_team_sept.nd_backend.appointment.payload.responses.ValidateResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public record AuthenticationService(
        RestTemplate restTemplate
) {
    public static final String url = "http://localhost:9000/api/v1";

    public ValidateResponse getAuthorization(HttpHeaders headers) {
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
        return null;
    }

    public UsersDataResponse getUsers(
            HttpHeaders headers,
            List<Long> ids,
            String subject
    ) {
        String uri = UriComponentsBuilder.fromHttpUrl(url + "/app/admin/" + subject)
                .queryParam("ids", ids)
                .encode()
                .toUriString();

        final var httpUserResponse = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<ResponseWrapper<UsersDataResponse>>() {
                }
        );

        if (httpUserResponse.getBody() != null && httpUserResponse.getBody().data.users != null) {
            return httpUserResponse.getBody().data;
        }
        return UsersDataResponse.build(List.of());
    }
}
