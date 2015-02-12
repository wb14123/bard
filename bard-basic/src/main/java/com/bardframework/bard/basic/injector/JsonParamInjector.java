package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.HandleError;
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
import com.github.drapostolos.typeparser.TypeParserException;

import java.io.IOException;
import java.util.HashMap;

@BindTo(JsonParam.class)
public class JsonParamInjector extends Injector<JsonParam> {
    @HandleErrors({
        @HandleError(code = BardBasicError.READ_JSON_ERROR,
            exception = JsonMappingException.class,
            description = "Read JSON data error"),
        @HandleError(code = 400, exception = TypeParserException.class, description = "params error")
    })
    @Before public void getJsonParam() throws IOException {
        context.putCustom("param", annotation.value());
        HashMap<String, Object> jsonMap = context.getCustom("jsonParam");
        if (jsonMap == null) {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            TypeReference<HashMap<String, Object>> typeRef =
                new TypeReference<HashMap<String, Object>>() {
                };
            jsonMap = mapper.readValue(context.getRequest().getInputStream(), typeRef);
            context.putCustom("jsonParam", jsonMap);
        }

        TypeParser parser = TypeParser.newBuilder().build();
        Object v = jsonMap.get(annotation.value());
        if (v == null) {
            return;
        }
        context.setInjectorVariable(parser.parse(v.toString(), context.getInjectorVariableType()));
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = context.getInjectorVariableType();
        docParameter.belongs = "json";
    }
}
