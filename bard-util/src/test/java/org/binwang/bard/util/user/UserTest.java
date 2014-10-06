package org.binwang.bard.util.user;

import org.binwang.bard.core.Handler;
import org.binwang.bard.core.Servlet;
import org.binwang.bard.util.user.marker.LoginToken;
import org.binwang.bard.util.user.marker.LoginUser;
import org.binwang.bard.util.user.marker.SignUp;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {
    private static String token = null;
    public Servlet servlet = null;
    public MockHttpServletRequest request;
    public HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new Servlet("org.binwang.bard.basic", "org.binwang.bard.util");
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servlet.addHandler(UserTestHandler.class);
    }

    @Test
    public void aSignUpTest() throws ServletException, IOException {
        request.setPathInfo("/signup");
        request.setParameter("username", "user");
        request.setParameter("password", "pass");
        servlet.service(request, response);
        assertEquals("user", response.getHeader("username"));
    }

    @Test
    public void bLoginTest() throws ServletException, IOException {
        request.setPathInfo("/login");
        request.setParameter("username", "user");
        request.setParameter("password", "pass");
        servlet.service(request, response);
        token = response.getHeader("token");
        assertNotNull(token);
    }

    @Test
    public void cLoginInvalidatePasswordTest() throws ServletException, IOException {
        request.setPathInfo("/login");
        request.setParameter("username", "user");
        request.setParameter("password", "invalidate");
        servlet.service(request, response);
        assertNull(response.getHeader("token"));
    }

    @Test
    public void dLoginUserTest() throws ServletException, IOException {
        request.setPathInfo("/auth");
        request.addHeader("auth-token", token);
        servlet.service(request, response);
        assertEquals("user", response.getHeader("username"));
    }

    @Test
    public void eLoginUserInvalidateTokenTest() throws ServletException, IOException {
        request.setPathInfo("/auth");
        request.addHeader("auth-token", "invalidate");
        servlet.service(request, response);
        assertNull(response.getHeader("username"));
    }


    public static class UserTestHandler extends Handler {
        @Path("/signup")
        @SignUp(TestUserDao.class)
        public void signUp(@QueryParam("username") String username) {
            User user = new User();
            user.username = username;
            new TestUserDao().getInstance().saveUser(user);
            context.response.setHeader("username", username);
        }

        @Path("/login")
        public void login(@LoginToken(TestUserDao.class) String token) {
            context.response.setHeader("token", token);
        }

        @Path("/auth")
        public void auth(@LoginUser(TestUserDao.class) User user) {
            context.response.setHeader("username", user.username);
        }

    }
}
