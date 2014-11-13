package com.bardframework.bard.basic;

import com.bardframework.bard.core.Servlet;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class GenericTester {
    protected Servlet servlet = null;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet() {
            @Override public String[] getPackageNames() {
                return new String[] {"com.bardframework.bard.basic"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
}
