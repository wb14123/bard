package org.binwang.bard.core;

import org.binwang.bard.core.defines.AddHeaderFilter;
import org.binwang.bard.core.defines.TrueAdapter1;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;

public class FilterTest {
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
    public void simpleFilterTest() {
        servlet.addHandler(SimpleFilterHandler.class);
        servlet.service(request, response);
        assertEquals("test_value", response.getHeader("test_name"));
    }

    public static class SimpleFilterHandler extends Handler {
        @TrueAdapter1
        @AddHeaderFilter(name="test_name1", value="test_value1")
        public void addHeader() {
        }
    }

}
