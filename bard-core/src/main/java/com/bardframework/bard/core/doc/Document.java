package com.bardframework.bard.core.doc;

import com.bardframework.bard.core.marker.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Model
public class Document {
    @JsonProperty(value = "APIs")
    @JsonPropertyDescription("The APIs")
    public List<Api> apis = new LinkedList<>();

    @JsonProperty(value = "name")
    @JsonPropertyDescription("The name of this document")
    public String name;

    @JsonProperty(value = "models")
    @JsonPropertyDescription("Models used in APIs. Include param and response types")
    public Map<String, JsonSchema> models = new HashMap<>();

    public static JsonSchema toJsonSchema(Class<?> c) throws JsonMappingException {
        if (c == null) {
            return null;
        }
        ObjectMapper m = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        m.acceptJsonFormatVisitor(m.constructType(c), visitor);
        return visitor.finalSchema();
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
