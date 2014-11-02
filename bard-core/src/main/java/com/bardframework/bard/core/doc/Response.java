package com.bardframework.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

public class Response implements Comparable<Response> {
    @JsonProperty(value = "code")
    public int code;

    @JsonProperty(value = "description")
    public String description;

    @JsonIgnore
    public Class<?> returnType;

    @JsonProperty(value = "schema")
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
