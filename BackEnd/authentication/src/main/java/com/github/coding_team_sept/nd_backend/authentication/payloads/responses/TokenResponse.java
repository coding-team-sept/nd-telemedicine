package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

public class TokenResponse extends DataResponse {
    public final String access;

    public TokenResponse(String access) {
        this.access = access;
    }

    public static TokenResponse build(String access) {
        return new TokenResponse(access);
    }
}
