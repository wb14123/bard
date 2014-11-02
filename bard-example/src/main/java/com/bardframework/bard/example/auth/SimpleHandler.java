package com.bardframework.bard.example.auth;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.basic.marker.LogRequest;
import com.bardframework.bard.basic.marker.Required;
import com.bardframework.bard.core.Handler;
import com.bardframework.bard.util.user.PasswordEncrypter;
import com.bardframework.bard.util.user.TokenStorage;
import com.bardframework.bard.util.user.marker.LoginUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.Map;

@LogRequest
@Produces("application/json")
public class SimpleHandler extends Handler {

    public static final Map<String, User> userStorage = new HashMap<>();

    @GET
    @Path("/signup")
    @Doc("Sign Up")
    public String signup(
        @Doc("Username") @QueryParam("username") @Required String username,
        @Doc("Password") @QueryParam("password") @Required String password) {
        if (userStorage.get(username) != null) {
            return "User already exist";
        }

        String[] result = PasswordEncrypter.encrypt(password);
        User user = new User();

        user.username = username;
        user.password = result[0];
        user.salt = result[1];

        userStorage.put(username, user);

        return "Sign up success";
    }

    @GET
    @Path("/login")
    @Doc("login")
    public String login(
        @Doc("Username") @QueryParam("username") @Required String username,
        @Doc("Password") @QueryParam("password") @Required String password) {

        User user = userStorage.get(username);

        if (user == null) {
            return "user not found";
        }

        if (!user.password.equals(PasswordEncrypter.encrypt(password, user.salt))) {
            return "Invalidate password";
        }

        return TokenStorage.put(username, 60 * 60 * 24);
    }

    @GET
    @Path("/auth")
    @Doc("auth test")
    public String auth(
        @LoginUser String username
    ) {
        return username;
    }


}
