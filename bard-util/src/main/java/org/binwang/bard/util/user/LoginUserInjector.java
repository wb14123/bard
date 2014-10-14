package org.binwang.bard.util.user;

import org.binwang.bard.basic.marker.Doc;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;
import org.binwang.bard.util.user.marker.LoginUser;

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
