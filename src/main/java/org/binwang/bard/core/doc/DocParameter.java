package org.binwang.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;

import java.util.HashMap;
import java.util.Map;

public class DocParameter {
    @JsonProperty(value = "name")
    public String name;

    @JsonProperty(value = "description")
    public String description;

    @JsonProperty(value = "belongs")
    public String belongs;

    @JsonIgnore
    public Class type;

    @JsonProperty(value = "limitations")
    public Map<String, Object> limitations = new HashMap<>();

    @JsonProperty(value = "schema")
    public JsonSchema getType() throws JsonMappingException {
        return Document.toJsonSchema(type);
    }
}
