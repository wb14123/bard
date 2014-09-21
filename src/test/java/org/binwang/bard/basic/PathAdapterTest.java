package org.binwang.bard.basic;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.Servlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PathAdapterTest {
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
    public void basicPathTest() throws ServletException, IOException {
        request.setPathInfo("/basic-abc");
        servlet.service(request, response);
        assertEquals("basic-abc", response.getHeader("path"));
    }

    @Test
    public void classPathTest() throws ServletException, IOException {
        request.setPathInfo("/class-abc/basic-abc");
        servlet.service(request, response);
        assertEquals("class-abc", response.getHeader("path"));
    }

    @Test
    public void paramPathTest() throws ServletException, IOException {
        request.setPathInfo("/param-abc/abc/def");
        servlet.service(request, response);
        assertEquals("abc", response.getHeader("param-a"));
        assertEquals("def", response.getHeader("param-b"));
    }

    public static class BasicPathHandler extends Handler {
        @Path("/basic-abc")
        public void handle() {
            context.response.addHeader("path", "basic-abc");
        }
    }


    @Path("/class-abc")
    public static class ClassPathHandler extends Handler {
        @Path("/basic-abc")
        public void handle() {
            context.response.addHeader("path", "class-abc");
        }
    }


    public static class ParamPathHandler extends Handler {
        @Path("/param-abc/{a}/{b}")
        public void handle(@PathParam("a") String a, @PathParam("b") String b) {
            context.response.addHeader("param-a", a);
            context.response.addHeader("param-b", b);
        }
    }
}
