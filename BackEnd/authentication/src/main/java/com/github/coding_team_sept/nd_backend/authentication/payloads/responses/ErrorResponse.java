package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.AppException;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message
) {
    public static ErrorResponse build(String message) {
        return new ErrorResponse(message);
    }

    public static ErrorResponse fromException(AppException e) {
        return build(e.message);
    }

    public static ErrorResponse fromException(Exception e) {
        return build(e.getMessage());
    }

    public static ErrorResponse fromUnknownException(Exception e) {
        return build("Unkown Error: " + e.getMessage());
    }
}
