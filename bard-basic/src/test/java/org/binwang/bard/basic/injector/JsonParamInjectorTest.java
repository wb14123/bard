package org.binwang.bard.basic.injector;

import org.binwang.bard.basic.GenericTester;
import org.binwang.bard.basic.marker.JsonParam;
import org.binwang.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonParamInjectorTest extends GenericTester {

    @Test
    public void simpleQueryTest() throws ServletException, IOException {
        request.setPathInfo("/simple-json");
        String json = "{\"a\": 1.1, \"b\": \"2.2\"}";
        request.setContent(json.getBytes());
        servlet.service(request, response);
        assertEquals("1.1", response.getHeader("simple-json-a"));
        assertEquals("2.2", response.getHeader("simple-json-b"));
    }

    public static class simpleJsonHandler extends Handler {
        @Path("/simple-json")
        public void handle(@JsonParam("a") String a, @JsonParam("b") String b) {
            context.response.addHeader("simple-json-a", a);
            context.response.addHeader("simple-json-b", b);
        }
    }
}
