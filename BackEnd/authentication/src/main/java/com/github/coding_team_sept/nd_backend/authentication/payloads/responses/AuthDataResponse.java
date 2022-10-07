package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;

public class AuthDataResponse extends DataResponse {
    public final String email;
    public final String name;
    public final String role;

    public AuthDataResponse() {
        this(null, null, null);
    }

    public AuthDataResponse(String email, String name, String role) {
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static AuthDataResponse fromUserDetails(AppUserDetails userDetails) {
        return new AuthDataResponse(userDetails.getEmail(), userDetails.getName(), userDetails.getRole().getName().name());
    }
}
