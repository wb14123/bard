package com.bardframework.bard.basic.filter;

import com.bardframework.bard.basic.ErrorResult;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Filter;
import com.bardframework.bard.core.doc.Response;
import com.bardframework.bard.core.marker.After;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Only support produce "application/json" for now.
 */
@BindTo(Produces.class)
public class ProduceFilter extends Filter<Produces> {
    private static ObjectMapper objectMapper;
    private String contentType;

    public ProduceFilter() {
        super();
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @After public void filterResult() throws IOException {
        if (contentType == null) {
            contentType = annotation.value()[0];
        }

        PrintWriter writer = context.response.getWriter();

        if (contentType.equals("application/json")) {
            context.response.addHeader("Content-Type", contentType);
            if (context.exception != null && !context.exceptionHandled) {
                context.result = new ErrorResult(500, "Unknown server error.");
            }

            try {
                String v = objectMapper.writeValueAsString(context.result);
                writer.print(v);
            } catch (JsonProcessingException e) {
                writer.print(e);
            } finally {
                writer.close();
            }
        } else {
            context.result = new ErrorResult(500, "Unknown server error.");
            context.exception = new ContentTypeNotSupportException(contentType);
            context.exceptionHandled = false;
            contentType = "application/json";
            filterResult();
        }
    }

    @Override public void generateDoc() {
        api.produces = annotation.value();
        Response response = new Response();
        response.code = 200;
        response.returnType = returnType;
        api.responses.add(response);
    }

    public static class ContentTypeNotSupportException extends Exception {
        public ContentTypeNotSupportException(String contentType) {
            super("Content type not support: " + contentType);
        }
    }
}
