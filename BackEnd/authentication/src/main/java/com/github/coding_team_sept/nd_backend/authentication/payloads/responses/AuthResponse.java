package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse extends DataResponse{
    public final TokenResponse token;
    public final AuthDataResponse user;

    public AuthResponse() {
        this(null, null);
    }

    public AuthResponse(TokenResponse token, AuthDataResponse user) {
        this.token = token;
        this.user = user;
    }

    public static AuthResponse fromToken(TokenResponse token) {
        return new AuthResponse(token, null);
    }
}
