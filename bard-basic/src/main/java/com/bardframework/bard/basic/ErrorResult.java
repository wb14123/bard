package com.bardframework.bard.basic;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@Model
public class ErrorResult {
    @JsonProperty
    @JsonPropertyDescription("The error code")
    public int code;

    @JsonProperty
    @JsonPropertyDescription("The error message")
    public String message;

    public ErrorResult() {

    }

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
