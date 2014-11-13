package com.bardframework.bard.core.doc;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

import java.util.HashMap;
import java.util.Map;

@Model
public class DocParameter implements Comparable<DocParameter> {
    @JsonProperty(value = "name")
    @JsonPropertyDescription("The name of this param")
    public String name;

    @JsonProperty(value = "description")
    @JsonPropertyDescription("The description of this param")
    public String description;

    @JsonProperty(value = "belongs")
    @JsonPropertyDescription("How to get the param from HTTP request?")
    public String belongs;

    @JsonIgnore
    public Class<?> type;

    @JsonProperty(value = "limitations")
    @JsonPropertyDescription("Limitations on this param")
    public Map<String, Object> limitations = new HashMap<>();

    @JsonProperty(value = "schema")
    @JsonPropertyDescription("The type of this param")
    public JsonSchema getType() throws JsonMappingException {
        return Document.toJsonSchema(type);
    }

    @Override public int compareTo(DocParameter o) {
        if (equal(o.name, name) &&
            equal(o.belongs, belongs) &&
            equal(o.description, description)) {
            return 0;
        }
        return 1;
    }

    @JsonIgnore
    public boolean isNull() {
        return name == null && description == null;
    }

    @JsonIgnore
    private boolean equal(String a, String b) {
        return a == null && b == null || a != null && a.equals(b);
    }
}
