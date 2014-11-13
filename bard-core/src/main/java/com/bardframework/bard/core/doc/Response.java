package com.bardframework.bard.core.doc;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

@Model
public class Response implements Comparable<Response> {
    @JsonProperty(value = "code")
    @JsonPropertyDescription("The response type of HTTP")
    public int code;

    @JsonProperty(value = "description")
    @JsonPropertyDescription("The description of this response")
    public String description;

    @JsonIgnore
    public Class<?> returnType;

    @JsonProperty(value = "schema")
    @JsonPropertyDescription("The return type.")
    public JsonSchema getReturn() throws JsonMappingException {
        return Document.toJsonSchema(returnType);
    }

    @Override public int compareTo(Response o) {
        if (o.description == null || description == null) {
            return 1;
        }
        return o.description.compareTo(description);
    }
}
