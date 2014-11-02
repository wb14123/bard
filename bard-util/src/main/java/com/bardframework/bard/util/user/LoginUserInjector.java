package com.bardframework.bard.util.user;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.basic.marker.Required;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.util.user.marker.LoginUser;

import javax.ws.rs.HeaderParam;

@BindTo(LoginUser.class)
public class LoginUserInjector extends Injector<LoginUser> {
    @Before
    public void getUser(@Doc("Auth token") @HeaderParam("auth-token") @Required String token) {
        injectorVariable = TokenStorage.get(token);
    }

    @Override public void generateDoc() {
        docParameter.description = "Username auto get by token";
    }
}
