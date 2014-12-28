package com.bardframework.bard.core.doc;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.lang.reflect.Method;
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
    @JsonIgnore
    private Method handlerMethod;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public Set<DocParameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<DocParameter> parameters) {
        this.parameters = parameters;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }
}
