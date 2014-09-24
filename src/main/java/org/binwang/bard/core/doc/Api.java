package org.binwang.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

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
    public Set<Response> responses = new TreeSet<>();

    @JsonProperty(value = "extensions")
    public Map<String, Object> extensions = new HashMap<>();

}
