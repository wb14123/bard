package org.binwang.bard.util.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.binwang.bard.basic.marker.ErrorCase;
import org.binwang.bard.basic.marker.HandleErrors;
import org.binwang.bard.basic.marker.Required;
import org.binwang.bard.core.BindTo;
import org.binwang.bard.core.Injector;
import org.binwang.bard.core.marker.Before;
import org.binwang.bard.util.user.marker.LoginToken;

import javax.ws.rs.QueryParam;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@BindTo(LoginToken.class)
public class LoginTokenInjector extends Injector<LoginToken> {
    @HandleErrors({
        @ErrorCase(code = 403, logLevel = "DEBUG", description = "Username or password error",
            exception = UserDao.UserNotFoundException.class),
        @ErrorCase(code = 500, logLevel = "DEBUG", description = "Salt not found for user",
            exception = UserDao.SaltNotFoundException.class),
        @ErrorCase(code = 500, logLevel = "DEBUG", description = "Encrypt password error",
            exception = NoSuchAlgorithmException.class)
    })
    @Before public void login(
        @QueryParam("username") @Required String username,
        @QueryParam("password") @Required String password)
        throws UserDao.UserNotFoundException, UserDao.SaltNotFoundException,
        NoSuchAlgorithmException, InstantiationException, IllegalAccessException {
        String salt = getUserDao().getSalt(username);
        String saltPassword = password + salt;
        String encryptPassword = DigestUtils.sha256Hex(saltPassword.getBytes());
        getUserDao().checkPassword(username, encryptPassword);
        String token = UUID.randomUUID().toString();
        getUserDao().saveToken(token, username, 1000 * 60 * 24 * 7);
        injectorVariable = token;
    }

    @Override public void generateDoc() {
    }

    private UserDao getUserDao() throws IllegalAccessException, InstantiationException {
        UserDao dao = annotation.value().newInstance();
        return dao.getInstance();
    }
}
