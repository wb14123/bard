package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.basic.marker.JsonParam;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.drapostolos.typeparser.TypeParser;

import java.io.IOException;
import java.util.HashMap;

@BindTo(JsonParam.class)
public class JsonParamInjector extends Injector<JsonParam> {
    @HandleErrors({
        @ErrorCase(code = BardBasicError.READ_JSON_ERROR, logLevel = "DEBUG",
            exception = JsonMappingException.class,
            description = "Read JSON data error")
    })
    @Before public void getJsonParam() throws IOException {
        injectContext.put("param", annotation.value());
        HashMap<String, Object> jsonMap = (HashMap<String, Object>) context.custom.get("jsonParam");
        if (jsonMap == null) {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };
            jsonMap = mapper.readValue(context.request.getInputStream(), typeRef);
            context.custom.put("jsonParam", jsonMap);
        }

        TypeParser parser = TypeParser.newBuilder().build();
        Object v = jsonMap.get(annotation.value());
        if (v == null) {
            return;
        }
        injectorVariable = parser.parse(v.toString(), injectorVariableType);
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "json";
    }
}
