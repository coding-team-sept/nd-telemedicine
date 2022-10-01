package com.github.coding_team_sept.nd_backend.appointment.payloads.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersDataResponse extends DataResponse {
    public final List<UserDataResponse> users;

    public UsersDataResponse() {
        this(null);
    }

    @JsonCreator
    public UsersDataResponse(List<UserDataResponse> users) {
        this.users = users;
    }

    public static UsersDataResponse build(List<UserDataResponse> users) {
        return new UsersDataResponse(users);
    }

    @JsonValue
    public List<UserDataResponse> getUsers() {
        return users;
    }
}
