package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDataResponse(
        String email,
        String name,
        String role
) {
    public static UserDataResponse build(String email, String name, String role) {
        return new UserDataResponse(email, name, role);
    }
    public static UserDataResponse fromUserDetails(AppUserDetails userDetails) {
        return build(userDetails.getEmail(), userDetails.getName(), userDetails.getRole().getName().name());
    }
}
