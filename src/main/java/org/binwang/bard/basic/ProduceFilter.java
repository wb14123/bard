package org.binwang.bard.basic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.binwang.bard.basic.marker.Produce;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Filter;
import org.binwang.bard.core.doc.Response;
import org.binwang.bard.core.marker.After;

import java.io.IOException;
import java.io.PrintWriter;

@BindTo(Produce.class)
public class ProduceFilter extends Filter<Produce> {
    private static ObjectMapper objectMapper;

    public ProduceFilter() {
        super();
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
    }

    @After public void filterResult() throws IOException {
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
