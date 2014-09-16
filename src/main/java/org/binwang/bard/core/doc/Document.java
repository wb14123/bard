package org.binwang.bard.core.doc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

import java.util.LinkedList;
import java.util.List;

public class Document {
    @JsonProperty(value = "APIs")
    public List<Api> apis = new LinkedList<>();

    public static JsonSchema toJsonSchema(Class c) throws JsonMappingException {
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
