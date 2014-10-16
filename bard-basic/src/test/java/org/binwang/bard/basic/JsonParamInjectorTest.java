package org.binwang.bard.basic;

import org.binwang.bard.basic.marker.JsonParam;
import org.binwang.bard.core.Handler;
import org.binwang.bard.core.Servlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonParamInjectorTest {

    public Servlet servlet = null;
    public MockHttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet("org.binwang.bard.basic");
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

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
