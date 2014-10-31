package org.binwang.bard.basic.injector;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.Servlet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DefaultValueInjectorTest {
    public Servlet servlet = null;
    public MockHttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet() {
            @Override protected String[] getPackageNames() {
                return new String[] {"org.binwang.bard.basic"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

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
            context.response.addHeader("a", a);
        }
    }


    public static class DefaultHasValueHandler extends Handler {
        @Path("/default-has-value-test")
        public void handle(@QueryParam("a") @DefaultValue("a") String a) {
            context.response.addHeader("a", a);
        }
    }
}
