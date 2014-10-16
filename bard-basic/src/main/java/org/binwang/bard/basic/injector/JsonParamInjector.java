package org.binwang.bard.basic.injector;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.basic.BardBasicError;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.basic.marker.JsonParam;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

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
