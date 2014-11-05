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


    // user handler
    protected <T> T signUp(String username, String password, Class<T> t)
        throws ServletException, IOException {
        newRequest();
        request.setPathInfo("/user/signup");
        request.setParameter("username", username);
        request.setParameter("password", password);
        request.setMethod("GET");
        return getResult(t);
    }

    protected <T> T login(String username, String password, Class<T> t)
        throws ServletException, IOException {
        newRequest();
        request.setPathInfo("/user/login");
        request.setParameter("username", username);
        request.setParameter("password", password);
        request.setMethod("GET");
        return getResult(t);
    }

    protected <T> T info(String token, Class<T> t) throws ServletException, IOException {
        newRequest();
        request.setPathInfo("/user/info");
        request.addHeader("auth-token", token);
        request.setMethod("GET");
        return getResult(t);
    }

    // article handler
    protected <T> T createArticle(String token, String title, String content, Class<T> t)
        throws ServletException, IOException {
        newRequest();
        String requestContent = "title=" + title + "&content=" + content;
        request.setPathInfo("/article");
        request.setContent(requestContent.getBytes());
        request.addHeader("auth-token", token);
        request.setMethod("PUT");
        return getResult(t);
    }

    protected <T> T getArticle(String token, String id, Class<T> t)
        throws ServletException, IOException {
        newRequest();
        request.setPathInfo("/article/" + id);
        request.setMethod("GET");
        request.addHeader("auth-token", token);
        return getResult(t);
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
