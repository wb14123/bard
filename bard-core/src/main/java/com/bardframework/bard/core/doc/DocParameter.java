package com.bardframework.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

import java.util.HashMap;
import java.util.Map;

public class DocParameter implements Comparable<DocParameter> {
    @JsonProperty(value = "name")
    public String name;

    @JsonProperty(value = "description")
    public String description;

    @JsonProperty(value = "belongs")
    public String belongs;

    @JsonIgnore
    public Class<?> type;

    @JsonProperty(value = "limitations")
    public Map<String, Object> limitations = new HashMap<>();

    @JsonProperty(value = "schema")
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

    private boolean equal(String a, String b) {
        return a == null && b == null || a != null && a.equals(b);
    }
}
