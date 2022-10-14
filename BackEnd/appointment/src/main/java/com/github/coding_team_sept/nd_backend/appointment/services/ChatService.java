package com.github.coding_team_sept.nd_backend.appointment.services;

import com.github.coding_team_sept.nd_backend.appointment.exceptions.RestClientException;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ChatResponse;
import com.github.coding_team_sept.nd_backend.appointment.payloads.responses.ResponseWrapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public record ChatService(
        RestTemplate restTemplate
) {
    public static final String url = "http://chat:9002/api/v1";

    ChatResponse getChatStatus(
            HttpHeaders headers,
            Long appointmentId
    ) throws RestClientException {
        try {
            final var response = restTemplate.exchange(
                    (url + "/chat/status/" + appointmentId),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<ResponseWrapper<ChatResponse>>() {
                    });

            if (response.getBody() != null && response.getBody().data != null) {
                return response.getBody().data;
            }
            throw RestClientException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Empty body");
        } catch (HttpClientErrorException e) {
            throw RestClientException.fromRestClientResponseException(e);
        }
    }
}
