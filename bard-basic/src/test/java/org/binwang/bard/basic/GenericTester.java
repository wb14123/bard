package org.binwang.bard.basic;

import org.binwang.bard.core.Servlet;
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
            @Override protected String[] getPackageNames() {
                return new String[] {"org.binwang.bard.basic"};
            }
        };
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
}
