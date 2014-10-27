package org.binwang.bard.basic.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binwang.bard.basic.ErrorResult;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.doc.Response;
import org.binwang.bard.core.marker.After;

import javax.ws.rs.Produces;
import java.io.IOException;
import java.io.PrintWriter;

@BindTo(Produces.class)
public class ProduceFilter extends Filter<Produces> {
    private static ObjectMapper objectMapper;

    public ProduceFilter() {
        super();
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @After public void filterResult() throws IOException {
        context.response.addHeader("Content-Type", annotation.value()[0]);

        if (context.exception != null && !context.exceptionHandled) {
            context.result = new ErrorResult(500, "Unknown server error.");
        }

        PrintWriter writer = context.response.getWriter();
        try {
            String v = objectMapper.writeValueAsString(context.result);
            writer.print(v);
        } catch (JsonProcessingException e) {
            writer.print(e);
        } finally {
            writer.close();
        }
    }

    @Override public void generateDoc() {
        api.produces = annotation.value();
        Response response = new Response();
        response.code = 200;
        response.returnType = returnType;
        api.responses.add(response);
    }
}
