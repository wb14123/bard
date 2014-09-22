package org.binwang.bard.basic.injector;

import com.github.drapostolos.typeparser.TypeParser;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;

import javax.ws.rs.FormParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@BindTo(FormParam.class)
public class FormParamInjector extends Injector<FormParam> {
    @Before public void getParam() throws IOException {
        // get from cache first
        Map<String, String> formParams = (Map<String, String>) context.custom.get("form");
        if (formParams == null) {
            formParams = new HashMap<>();
            BufferedReader reader = context.request.getReader();
            String line = reader.readLine();
            String[] pairs = line.split("&");

            for (String pair : pairs) {
                String[] fields = pair.split("=");
                String name = URLDecoder.decode(fields[0], "UTF-8");
                String value = URLDecoder.decode(fields[1], "UTF-8");
                formParams.put(name, value);
            }

            context.custom.put("form", formParams);
        }

        TypeParser parser = TypeParser.newBuilder().build();
        String param = formParams.get(annotation.value());
        injectorVariable = parser.parse(param, injectorVariableType);
    }

    @Override public void generateDoc() {
    }
}
