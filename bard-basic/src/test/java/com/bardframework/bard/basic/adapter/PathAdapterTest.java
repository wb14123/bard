package com.bardframework.bard.basic.adapter;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PathAdapterTest extends GenericTester {
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

    @Test
    public void pathRootTest() throws ServletException, IOException {
        request.setPathInfo("/path-root-test");
        servlet.service(request, response);
        assertEquals("true", response.getHeader("path-root"));
    }

    @Test
    public void pathRootSlashTest() throws ServletException, IOException {
        request.setPathInfo("/path-root-test/");
        servlet.service(request, response);
        assertEquals("true", response.getHeader("path-root"));
    }

    @Test
    public void pathWithSubTest() throws ServletException, IOException {
        request.setPathInfo("/path-root-test/sub");
        servlet.service(request, response);
        assertEquals("true", response.getHeader("with-sub"));
    }

    public static class BasicPathHandler extends Handler {
        @Path("/basic-abc")
        public void handle() {
            context.getResponse().addHeader("path", "basic-abc");
        }
    }


    @Path("/class-abc")
    public static class ClassPathHandler extends Handler {
        @Path("/basic-abc")
        public void handle() {
            context.getResponse().addHeader("path", "class-abc");
        }
    }


    public static class ParamPathHandler extends Handler {
        @Path("/param-abc/{a}/{b}")
        public void handle(@PathParam("a") String a, @PathParam("b") String b) {
            context.getResponse().addHeader("param-a", a);
            context.getResponse().addHeader("param-b", b);
        }
    }


    @Path("/path-root-test")
    public static class PathTestRootHandler extends Handler {
        @Path("/{a}")
        public void withSub() {
            context.getResponse().addHeader("with-sub", "true");
        }

        @Path("/")
        public void root() {
            context.getResponse().addHeader("path-root", "true");
        }
    }
}
