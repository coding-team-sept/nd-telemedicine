package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import lombok.Builder;

import java.util.List;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppResponse(Object data, Object error) {
    public static AppResponse error(ErrorResponse response) {
        return AppResponse.builder().error(response).build();
    }

    public static AppResponse data(Object data) {
        return AppResponse.builder().data(data).build();
    }

    public static AppResponse auth(AuthResponse response) {
        return AppResponse.data(response);
    }

    public static AppResponse login(String token, AppUserDetails userDetails) {
        return AppResponse.data(AuthResponse.build(
                TokenResponse.build(token),
                UserDataResponse.fromUserDetails(userDetails)
        ));
    }

    public static AppResponse user(AppUserResponse appUserResponse) {
        return AppResponse.data(appUserResponse);
    }

    public static AppResponse user(List<AppUserResponse> appUserResponses) {
        return AppResponse.data(appUserResponses);
    }
}
