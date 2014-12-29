package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.ErrorResult;
import org.apache.saltedpeanuts.TestServer;
import org.apache.saltedpeanuts.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserHandlerTest extends TestServer {

    @Test
    public void testSignup() throws Exception {
        User user = UserHandlerTester.signup("user1" , "pass1" , null, User.class);
        assertNotNull(user.id);
        assertEquals("user1" , user.username);
        assertNull(user.password);
    }

    @Test
    public void testDuplicateSignup() throws Exception {
        UserHandlerTester.signup("user2" , "pass2" , null, User.class);
        ErrorResult error = UserHandlerTester.signup("user2" , "pass3" , null, ErrorResult.class);
        assertEquals(20000, error.code);
    }

    @Test
    public void testLogin() throws Exception {
        UserHandlerTester.signup("user3" , "pass3" , null, User.class);
        UserHandler.TokenResult result = UserHandlerTester.login("user3" , "pass3" ,
            UserHandler.TokenResult.class);
        assertNotNull(result.token);
    }

    @Test
    public void testLoginInvalidatePassword() throws Exception {
        UserHandlerTester.signup("user4" , "pass4" , null, User.class);
        ErrorResult result = UserHandlerTester.login("user4" , "pass5" , ErrorResult.class);
        assertEquals(20001, result.code);
    }

    @Test
    public void testLoginNoUsername() throws Exception {
        ErrorResult result = UserHandlerTester.login("user1111" , "pass6" , ErrorResult.class);
        assertEquals(20001, result.code);

    }

    @Test
    public void testInfo() throws Exception {
        UserHandlerTester.signup("user5" , "pass5" , null, User.class);
        UserHandler.TokenResult result = UserHandlerTester.login("user5" , "pass5" ,
            UserHandler.TokenResult.class);
        User user = UserHandlerTester.info(result.token, User.class);
        assertEquals("user5" , user.username);
    }

    @Test
    public void testInfoNoAuth() throws Exception {
        ErrorResult result = UserHandlerTester.info("" , ErrorResult.class);
        assertEquals(403, result.code);
    }



}
