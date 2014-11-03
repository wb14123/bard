package org.apache.saltedpeanuts.handler;

import com.bardframework.bard.basic.marker.*;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.util.db.marker.DBSession;
import com.bardframework.bard.util.user.PasswordEncrypter;
import com.bardframework.bard.util.user.TokenStorage;
import com.bardframework.bard.util.user.marker.LoginUser;
import org.apache.saltedpeanuts.model.User;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@LogRequest
@CORSHeaders
@Produces("application/json")
@Path("/user")
public class UserHandler extends Handler {

    @DBSession public Session dbSession;

    @Doc("Sign up a user")
    @GET
    @Path("/signup")
    @HandleErrors({
        @ErrorCase(code = 20000, logLevel = "DEBUG",
            exception = UsernameDuplicateException.class,
            description = "Username duplicate")
    })
    public User signup(
        @QueryParam("username") @Required String username,
        @QueryParam("password") @Required String password,
        @QueryParam("email") String email
    ) throws UsernameDuplicateException {
        User user = User.getUserByUsername(dbSession, username);
        if (user != null) {
            throw new UsernameDuplicateException(username);
        }
        user = new User();
        user.username = username;
        user.email = email;
        String[] result = PasswordEncrypter.encrypt(password);
        user.password = result[0];
        user.salt = result[1];
        dbSession.save(user);

        return user;
    }

    @Doc("User login. Returns the auth token.")
    @GET
    @Path("/login")
    @HandleErrors({
        @ErrorCase(code = 20001, logLevel = "DEBUG",
            exception = InvalidatePasswordException.class,
            description = "invalidate password")
    })
    public TokenResult login(
        @QueryParam("username") @Required String username,
        @QueryParam("password") @Required String password
    ) throws InvalidatePasswordException {
        User user = User.getUserByUsername(dbSession, username);
        if (user == null ||
            !user.password.equals(PasswordEncrypter.encrypt(password, user.salt))) {
            throw new InvalidatePasswordException();
        }
        TokenResult result = new TokenResult();
        result.token = TokenStorage.put(user.id, 60 * 60 * 24);
        return result;
    }

    @Doc("Get the user info")
    @GET
    @Path("/info")
    public User info(@LoginUser @Required String id) {
        User user = new User();
        dbSession.load(user, id);
        return user;
    }

    public static class TokenResult {
        public String token;
    }


    public static class UsernameDuplicateException extends Exception {
        public UsernameDuplicateException(String username) {
            super("Username duplicate: " + username);
        }
    }


    public static class InvalidatePasswordException extends Exception {
        public InvalidatePasswordException() {
            super("Invalidate username or password");
        }
    }
}
