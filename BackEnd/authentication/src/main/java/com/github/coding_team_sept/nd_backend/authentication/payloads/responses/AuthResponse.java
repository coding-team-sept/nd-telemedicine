package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
        TokenResponse token,
        UserDataResponse user
) {
    public static AuthResponse build(TokenResponse token, UserDataResponse user) {
        return new AuthResponse(token, user);
    }

    public static AuthResponse token(TokenResponse token) {
        return new AuthResponse(token, null);
    }
}
