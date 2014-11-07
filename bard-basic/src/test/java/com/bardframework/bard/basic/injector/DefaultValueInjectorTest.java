package com.bardframework.bard.basic.injector;

import com.bardframework.bard.basic.GenericTester;
import com.bardframework.bard.core.Handler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DefaultValueInjectorTest extends GenericTester {

    @Test
    public void defaultTest() throws ServletException, IOException {
        request.setPathInfo("/default-test");
        servlet.service(request, response);
        assertEquals("a", response.getHeader("a"));
    }

    @Test
    public void defaultHasValueTest() throws ServletException, IOException {
        request.setPathInfo("/default-has-value-test");
        request.setParameter("a", "b");
        servlet.service(request, response);
        assertEquals("b", response.getHeader("a"));
    }


    public static class DefaultHandler extends Handler {
        @Path("/default-test")
        public void handle(@DefaultValue("a") String a) {
            context.getResponse().addHeader("a", a);
        }
    }


    public static class DefaultHasValueHandler extends Handler {
        @Path("/default-has-value-test")
        public void handle(@QueryParam("a") @DefaultValue("a") String a) {
            context.getResponse().addHeader("a", a);
        }
    }
}
