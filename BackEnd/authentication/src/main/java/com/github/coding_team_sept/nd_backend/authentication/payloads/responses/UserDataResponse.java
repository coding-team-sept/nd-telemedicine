package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

public class UserDataResponse extends DataResponse {
    public final Long id;
    public final String email;
    public final String name;

    public UserDataResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public static UserDataResponse build(Long id, String email, String name) {
        return new UserDataResponse(id, email, name);
    }
}
