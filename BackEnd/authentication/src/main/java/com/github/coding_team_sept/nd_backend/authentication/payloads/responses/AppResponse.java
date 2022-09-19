package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppResponse(Object data, Object error) {
    public static AppResponse error(Object error) {
        return AppResponse.builder().error(error).build();
    }

    public static AppResponse error(String message) {
        return AppResponse.error(new ErrorResponse(message));
    }

    public static AppResponse data(Object data) {
        return AppResponse.builder().data(data).build();
    }

    public static AppResponse register(String token) {
        return AppResponse.data(Map.of(
                "token", new TokenResponse(token)
        ));
    }

    public static AppResponse login(String token, AppUserDetails userDetails) {
        return AppResponse.data(Map.of(
                "token", new TokenResponse(token),
                "user", Map.of(
                        "id", userDetails.getId(),
                        "email", userDetails.getEmail(),
                        "name", userDetails.getName(),
                        "role", userDetails.getRole().getName().name()
                )
        ));
    }

    public static AppResponse user(AppUserResponse appUserResponse) {
        return AppResponse.data(appUserResponse);
    }

    public static AppResponse user(List<AppUserResponse> appUserResponses) {
        return AppResponse.data(appUserResponses);
    }
}
