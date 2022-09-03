package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppResponse(Map<String, Object> data, Map<String, Object> error) {
    public static AppResponse error(String message, Exception e) {
        return AppResponse.builder().error(Map.of(
                "message", (message == null) ? e.getMessage() : message
        )).build();
    }

    public static AppResponse register(String token) {
        return AppResponse.builder().data(Map.of(
                "token", Map.of(
                        "token", token
                )
        )).build();
    }

    public static AppResponse login(String token, AppUserDetails userDetails) {
        return AppResponse.builder().data(Map.of(
                "token", Map.of(
                        "token", token
                ),
                "user", Map.of(
                        "id", userDetails.getId(),
                        "email", userDetails.getEmail(),
                        "name", userDetails.getName(),
                        "role", userDetails.getRole().getName().name()
                )
        )).build();
    }
}
