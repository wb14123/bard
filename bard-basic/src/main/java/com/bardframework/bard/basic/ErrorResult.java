package com.bardframework.bard.basic;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

@Model
public class ErrorResult {
    @JsonProperty
    public int code;

    @JsonProperty
    public String message;

    public ErrorResult() {

    }

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
