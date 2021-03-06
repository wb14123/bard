package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class QueryParamInjectorTest extends GenericTester {

    @Test
    public void simpleQueryTest() throws ServletException, IOException {
        request.setPathInfo("/simple-query");
        request.setParameter("a", "1.1");
        request.setParameter("b", "2.2");
        servlet.service(request, response);
        assertEquals("1.1", response.getHeader("simple-query-a"));
        assertEquals("2.2", response.getHeader("simple-query-b"));
    }

    public static class simpleQueryHandler extends Handler {
        @Path("/simple-query")
        public void handle(@QueryParam("a") String a, @QueryParam("b") String b) {
            context.getResponse().addHeader("simple-query-a", a);
            context.getResponse().addHeader("simple-query-b", b);
        }
    }
}
