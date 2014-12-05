package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.ErrorResult;
import org.apache.saltedpeanuts.GenericTester;
import org.apache.saltedpeanuts.model.User;
import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.*;

public class UserHandlerTest extends GenericTester {

    @Test
    public void testSignup() throws Exception {
        User user = signUp("user1", "pass1", User.class);
        assertNotNull(user.id);
        assertEquals("user1", user.username);
        assertNull(user.password);
    }

    @Test
    public void testDuplicateSignup() throws ServletException, IOException {
        signUp("user2", "pass2", User.class);
        ErrorResult error = signUp("user2", "pass3", ErrorResult.class);
        assertEquals(20000, error.code);
    }

    @Test
    public void testLogin() throws Exception {
        signUp("user3", "pass3", User.class);
        UserHandler.TokenResult result = login("user3", "pass3", UserHandler.TokenResult.class);
        assertNotNull(result.token);
    }

    @Test
    public void testLoginInvalidatePassword() throws Exception {
        signUp("user4", "pass4", User.class);
        ErrorResult result = login("user4", "pass5", ErrorResult.class);
        assertEquals(20001, result.code);
    }

    @Test
    public void testLoginNoUsername() throws Exception {
        ErrorResult result = login("user1111", "pass6", ErrorResult.class);
        assertEquals(20001, result.code);

    }

    @Test
    public void testInfo() throws Exception {
        signUp("user5", "pass5", User.class);
        UserHandler.TokenResult result = login("user5", "pass5", UserHandler.TokenResult.class);
        User user = info(result.token, User.class);
        assertEquals("user5", user.username);
    }

    @Test
    public void testInfoNoAuth() throws Exception {
        ErrorResult result = info("", ErrorResult.class);
        assertEquals(5000, result.code);
    }



}
