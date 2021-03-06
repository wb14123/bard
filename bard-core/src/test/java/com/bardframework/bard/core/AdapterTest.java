package com.bardframework.bard.core;

import com.bardframework.bard.core.defines.FalseAdapter1;
import com.bardframework.bard.core.defines.FalseAdapter2;
import com.bardframework.bard.core.defines.TrueAdapter1;
import com.bardframework.bard.core.defines.TrueAdapter2;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdapterTest {
    public Servlet servlet = null;
    public HttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException,
        NoSuchFieldException, JsonMappingException {
        servlet = new Servlet() {
            @Override public String[] getPackageNames() {
                return new String[] {"com.bardframework.bard.core.defines"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void allTrueAdapterTest() {
        servlet.addHandler(AllTrueAdapterHandler.class);
        servlet.service(request, response);
        Assert.assertEquals("all_true", response.getHeader("test"));
        Assert.assertEquals("true", response.getHeader("after"));
        servlet.removeHandler(AllTrueAdapterHandler.class);
    }

    @Test
    public void allFalseAdapterTest() {
        servlet.addHandler(AllFalseAdapterHandler.class);
        servlet.service(request, response);
        Assert.assertEquals(null, response.getHeader("test"));
        Assert.assertEquals("true", response.getHeader("after"));
        servlet.removeHandler(AllFalseAdapterHandler.class);
    }

    @Test
    public void pickAdapterTest() {
        servlet.addHandler(AdapterHandler.class);
        servlet.service(request, response);
        Assert.assertEquals("added", response.getHeader("test_true"));
        Assert.assertEquals(null, response.getHeader("test_false"));
        servlet.removeHandler(AdapterHandler.class);
    }

    @Test
    public void classAdapterTest() {
        servlet.addHandler(ClassAdapterHandler.class);
        servlet.service(request, response);
        Assert.assertEquals("added", response.getHeader("test_adapter"));
        Assert.assertEquals(null, response.getHeader("test_no_adapter"));
        servlet.removeHandler(ClassAdapterHandler.class);
    }


    public static class AllTrueAdapterHandler extends Handler {
        @TrueAdapter1
        @TrueAdapter2
        public void testAllTrue() {
            context.response.addHeader("test", "all_true");
        }
    }


    public static class AllFalseAdapterHandler extends Handler {
        @FalseAdapter1
        @FalseAdapter2
        public void testAllFalse() {
            context.response.addHeader("test", "all_true");
        }
    }


    public static class AdapterHandler extends Handler {
        @FalseAdapter1
        public void notHere() {
            context.response.addHeader("test_false", "added");
        }

        @TrueAdapter1
        public void here() {
            context.response.addHeader("test_true", "added");
        }
    }


    @TrueAdapter1
    public static class ClassAdapterHandler extends Handler {
        // method with no adapter will not run
        public void notHere() {
            context.response.addHeader("test_no_adapter", "added");
        }

        @TrueAdapter1
        public void here() {
            context.response.addHeader("test_adapter", "added");
        }
    }

}
