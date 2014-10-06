package org.binwang.bard.util.user;

import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;
import org.binwang.bard.util.user.marker.LoginUser;

import javax.ws.rs.HeaderParam;

public class LoginUserInjector extends Injector<LoginUser> {
    @Before
    @HandleErrors({
        @ErrorCase(code = 403, logLevel = "DEBUG", description = "token not found",
            exception = UserDao.TokenNotFoundException.class),
        @ErrorCase(code = 500, logLevel = "DEBUG", description = "user not found",
            exception = UserDao.UserNotFoundException.class)
    })
    public void getUser(@HeaderParam("auth-token") @Required String token)
        throws UserDao.TokenNotFoundException, UserDao.UserNotFoundException,
        InstantiationException, IllegalAccessException {
        Long userId = getUserDao().getUserIdFromToken(token);
        injectorVariable = getUserDao().getUser(userId);
    }

    @Override public void generateDoc() {
    }

    private UserDao getUserDao() throws IllegalAccessException, InstantiationException {
        UserDao dao = annotation.value().newInstance();
        return dao.getInstance();
    }
}
