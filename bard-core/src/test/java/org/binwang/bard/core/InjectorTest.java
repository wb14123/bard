package org.binwang.bard.core;

import org.binwang.bard.core.defines.ExceptionInjector;
import org.binwang.bard.core.defines.IntegerZeroInjector;
import org.binwang.bard.core.defines.TrueAdapter1;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class InjectorTest {
    public Servlet servlet = null;
    public HttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet("org.binwang.bard.core.defines");
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void simpleInjectorTest() {
        servlet.addHandler(SimpleInjectorHandler.class);
        servlet.service(request, response);
        assertEquals("0", response.getHeader("handler_header"));
    }

    @Test
    public void fieldInjectorTest() {
        servlet.addHandler(FieldInjectorHandler.class);
        servlet.service(request, response);
        assertEquals("0", response.getHeader("handler_header"));
    }

    @Test
    public void exceptionInjectorTest() {
        servlet.addHandler(ExceptionInjectorHandler.class);
        servlet.service(request, response);
        assertEquals("true", response.getHeader("get_exception"));
    }

    @Test
    public void filedExceptionInjectorTest() {
        servlet.addHandler(FieldExceptionInjectorHandler.class);
        servlet.service(request, response);
        assertEquals("true", response.getHeader("get_exception"));
    }

    public static class SimpleInjectorHandler extends Handler {
        @TrueAdapter1
        public void addHeader(@IntegerZeroInjector Integer zero) {
            context.response.setHeader("handler_header", zero.toString());
        }
    }


    public static class FieldInjectorHandler extends Handler {
        @IntegerZeroInjector
        public Integer zero;

        @TrueAdapter1
        public void addHeader() {
            context.response.setHeader("handler_header", zero.toString());
        }
    }


    public static class ExceptionInjectorHandler extends Handler {
        @TrueAdapter1
        public void exceptionHandler(@ExceptionInjector Object whatever) {
        }
    }


    public static class FieldExceptionInjectorHandler extends Handler {
        @ExceptionInjector
        public Object whatever;

        @TrueAdapter1
        public void exceptionHandler() {
        }
    }

}
