package org.binwang.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Api {
    @JsonProperty(value = "description")
    public String description;

    @JsonProperty(value = "path")
    public String path;

    @JsonProperty(value = "method")
    public String method;

    @JsonProperty(value = "produces")
    public String[] produces;

    @JsonProperty(value = "consumes")
    public String consumes;

    @JsonProperty(value = "parameters")
    public List<DocParameter> parameters = new LinkedList<>();

    @JsonProperty(value = "responses")
    public List<Response> responses = new LinkedList<>();

    @JsonProperty(value = "extensions")
    public Map<String, Object> extensions = new HashMap<>();

}
