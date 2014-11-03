package org.apache.saltedpeanuts;

import com.bardframework.bard.core.Servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

public class GenericTester {
    protected Servlet servlet = null;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        newRequest();
    }

    /**
     * Clean up servlet, request and response.
     */
    protected void newRequest() {
        servlet = new SimpleServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    /**
     * Get result from response. Use JSON mapper to decode.
     *
     * @param t   The class to decode by JSON mapper.
     * @param <T> The class to decode by JSON mapper.
     * @return The decoded value.
     * @throws ServletException
     * @throws IOException
     */
    protected <T> T getResult(Class<T> t) throws ServletException, IOException {
        servlet.service(request, response);
        String content = response.getContentAsString();
        return mapper.readValue(content, t);
    }
}
