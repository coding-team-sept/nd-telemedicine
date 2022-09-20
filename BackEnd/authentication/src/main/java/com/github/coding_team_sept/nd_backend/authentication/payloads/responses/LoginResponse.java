package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

public record LoginResponse(
        TokenResponse token,
        UserDataResponse user
) {
    public static LoginResponse build(TokenResponse token, UserDataResponse user) {
        return new LoginResponse(token, user);
    }
}
