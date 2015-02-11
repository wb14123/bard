package com.bardframework.bard.util.user;

import com.bardframework.bard.basic.marker.Doc;
import com.bardframework.bard.basic.marker.ErrorCase;
import com.bardframework.bard.basic.marker.HandleErrors;
import com.bardframework.bard.core.BindTo;
import com.bardframework.bard.core.Injector;
import com.bardframework.bard.core.marker.Before;
import com.bardframework.bard.util.user.marker.LoginUser;

import javax.ws.rs.HeaderParam;

@BindTo(LoginUser.class)
public class LoginUserInjector extends Injector<LoginUser> {
    @Before
    @HandleErrors({
        @ErrorCase(exception = LoginUserInjector.NoAuthException.class, code = 403, description = "No Auth")
    })
    public void getUser(@Doc("Auth token") @HeaderParam("auth-token") String token)
        throws NoAuthException {
        if (token == null) {
            if (annotation.required()) {
                throw new NoAuthException();
            }
            return;
        }
        String uid = TokenStorage.get(token);
        if (uid == null && annotation.required()) {
            throw new NoAuthException();
        }
        context.setInjectorVariable(TokenStorage.get(token));
    }

    @Override public void generateDoc() {
    }


    public class NoAuthException extends Exception {
        public NoAuthException() {
            super("Auth Error");
        }
    }
}
