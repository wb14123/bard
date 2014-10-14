package org.binwang.bard.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.binwang.bard.core.marker.Model;

@Model
public class ErrorResult {
    @JsonProperty
    public int code;

    @JsonProperty
    public String message;
}
