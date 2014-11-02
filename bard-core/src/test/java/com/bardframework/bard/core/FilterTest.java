package com.bardframework.bard.core;

import com.bardframework.bard.core.defines.AddHeaderFilter;
import com.bardframework.bard.core.defines.ExceptionFilter;
import com.bardframework.bard.core.defines.TrueAdapter1;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class FilterTest {
    public Servlet servlet = null;
    public HttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet() {
            @Override protected String[] getPackageNames() {
                return new String[] {"com.bardframework.bard.core.defines"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void simpleFilterTest() {
        servlet.addHandler(SimpleFilterHandler.class);
        servlet.service(request, response);
        assertEquals("test_value", response.getHeader("test_name"));
        assertEquals("handler_value", response.getHeader("handler_header"));
        servlet.removeHandler(SimpleFilterHandler.class);
    }

    @Test
    public void exceptionFilterTest() {
        servlet.addHandler(ExceptionFilterHandler.class);
        servlet.service(request, response);
        assertEquals("test_value", response.getHeader("test_name"));
        assertEquals(null, response.getHeader("handler_header"));
        assertEquals("true", response.getHeader("exception"));
        servlet.removeHandler(SimpleFilterHandler.class);
    }

    public static class SimpleFilterHandler extends Handler {
        @TrueAdapter1
        @AddHeaderFilter(name = "test_name", value = "test_value")
        public void addHeader() {
            context.response.setHeader("handler_header", "handler_value");
        }
    }


    public static class ExceptionFilterHandler extends Handler {
        @TrueAdapter1
        @AddHeaderFilter(name = "test_name", value = "test_value")
        @ExceptionFilter
        public void exception() {
            context.response.setHeader("handler_header", "handler_value");
        }
    }

}
