package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.BardBasicError;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.github.drapostolos.typeparser.TypeParser;

import javax.ws.rs.FormParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

@BindTo(FormParam.class)
public class FormParamInjector extends Injector<FormParam> {
    @Before
    @HandleErrors({
        @ErrorCase(description = "read form data error",
            code = BardBasicError.READ_FORM_ERROR,
            exception = InvalidateFormException.class, logLevel = "DEBUG")
    })
    public void getParam() throws IOException, InvalidateFormException {
        injectContext.put("param", annotation.value());
        // get from cache first
        @SuppressWarnings("unchecked")
        Map<String, String> formParams = (Map<String, String>) context.custom.get("form");
        if (formParams == null) {
            formParams = new HashMap<>();
            BufferedReader reader = context.request.getReader();
            String line = reader.readLine();
            try {
                String[] pairs = line.split("&");

                for (String pair : pairs) {
                    String[] fields = pair.split("=");
                    String name = URLDecoder.decode(fields[0], "UTF-8");
                    String value = URLDecoder.decode(fields[1], "UTF-8");
                    formParams.put(name, value);
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                throw new InvalidateFormException("Invalidate form format", e);
            }

            context.custom.put("form", formParams);
        }

        String param = formParams.get(annotation.value());
        if (param == null) {
            injectorVariable = null;
            return;
        }
        TypeParser parser = TypeParser.newBuilder().build();
        injectorVariable = parser.parse(param, injectorVariableType);
    }

    @Override public void generateDoc() {
        docParameter.name = annotation.value();
        docParameter.type = injectorVariableType;
        docParameter.belongs = "form";
    }

    public static class InvalidateFormException extends Exception {
        public static final long serialVersionUID = 1L;

        public InvalidateFormException(String msg, Exception e) {
            super(msg, e);
        }
    }
}