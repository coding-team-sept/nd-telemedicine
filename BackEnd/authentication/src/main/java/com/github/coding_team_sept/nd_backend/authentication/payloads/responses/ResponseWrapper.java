package com.github.coding_team_sept.nd_backend.authentication.payloads.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper<T extends DataResponse> {
    public final T data;

    public ResponseWrapper(T data) {
        this.data = data;
    }

    public static <T extends DataResponse> ResponseWrapper<T> fromData(T data) {
        return new ResponseWrapper<>(data);
    }
}
