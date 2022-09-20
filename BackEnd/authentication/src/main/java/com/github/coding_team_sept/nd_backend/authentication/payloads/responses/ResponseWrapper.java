package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T extends DataResponse, R extends ErrorResponse> {
    public final T data;
    public final R error;

    public ResponseWrapper(T data, R error) {
        this.data = data;
        this.error = error;
    }

    public static <T extends DataResponse, R extends ErrorResponse> ResponseWrapper<T, R> fromData(T data) {
        return new ResponseWrapper<>(data, null);
    }

    public static <T extends DataResponse, R extends ErrorResponse> ResponseWrapper<T, R> fromError(R error) {
        return new ResponseWrapper<>(null, error);
    }

    public static <T extends DataResponse, R extends ErrorResponse> ResponseWrapper<T, R> empty() {
        return new ResponseWrapper<>(null, null);
    }
}
