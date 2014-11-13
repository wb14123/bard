package com.bardframework.bard.core.doc;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Model
public class Api {
    @JsonProperty(value = "description")
    @JsonPropertyDescription("The description of this API")
    public String description;

    @JsonProperty(value = "path")
    @JsonPropertyDescription("The request path")
    public String path;

    @JsonProperty(value = "method")
    @JsonPropertyDescription("The request method")
    public String method;

    @JsonProperty(value = "produces")
    @JsonPropertyDescription("The content type it produces")
    public String[] produces;

    @JsonProperty(value = "consumes")
    @JsonPropertyDescription("The content type it consumes")
    public String[] consumes;

    @JsonProperty(value = "parameters")
    @JsonPropertyDescription("The parameters")
    public Set<DocParameter> parameters = new TreeSet<>();

    @JsonProperty(value = "responses")
    @JsonPropertyDescription("The responses")
    public Set<Response> responses = new TreeSet<>();

    @JsonProperty(value = "extensions")
    @JsonPropertyDescription("Custom extension map")
    public Map<String, Object> extensions = new HashMap<>();

}
